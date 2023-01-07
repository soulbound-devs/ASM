package net.vakror.soulbound.items.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.networking.ModPackets;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.wand.IWandTier;
import net.vakror.soulbound.wand.ItemWandProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.item.HoeItem.changeIntoState;

public class WandItem extends DiggerItem {

    private final IWandTier tier;
    private ItemStack stack = null;

    public WandItem(Properties properties, IWandTier tier) {
        super(3, -3, Tiers.DIAMOND, BlockTags.create(new ResourceLocation(SoulboundMod.MOD_ID, "none")), properties);
        this.tier = tier;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (TierSortingRegistry.isCorrectTierForDrops(getTier(), state)) {
            boolean hasAxing = hasSeal("axing", stack);
            boolean hasPickaxing = hasSeal("pickaxing", stack);
            boolean hasHoeing = hasSeal("hoeing", stack);

            if (hasAxing && state.is(BlockTags.MINEABLE_WITH_AXE)) {
                System.out.println("Can mine with axe");
                return true;
            }
            if (hasPickaxing && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                System.out.println("Can mine with pickaxe");
                return true;
            }
            if (hasHoeing && state.is(BlockTags.MINEABLE_WITH_HOE)) {
                System.out.println("Can mine with hoe");
                return true;
            }
        }
        return false;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            if (pPlayer.isShiftKeyDown()) {
                pPlayer.getItemInHand(pUsedHand).getCapability(ItemWandProvider.WAND).ifPresent(itemWand -> {
                    itemWand.setSelectedSealSlot(itemWand.getSelectedSealSlot() + 1);
                    if (itemWand.getSelectedSealSlot() > tier.getActivatableSlots()) {
                        itemWand.setSelectedSealSlot(1);
                        itemWand.setSelectedIsAttack(false);
                    }
                    else if (itemWand.getSelectedSealSlot() > tier.getPassiveSlots()) {
                        itemWand.setSelectedIsAttack(true);
                    }
                    itemWand.setActiveSeal(null);
                    String mode = itemWand.isSelectedIsAttack() ? "Offensive/Defensive": "Passive";
                    int readableSlot = itemWand.isSelectedIsAttack() ? itemWand.getSelectedSealSlot() - tier.getPassiveSlots(): itemWand.getSelectedSealSlot();
                    String selectedSealName = capitalizeString(itemWand.getAllActivatableSeals().get(itemWand.getSelectedSealSlot() - 1).getId());
                    ((ServerPlayer) pPlayer).connection.send(new ClientboundSetActionBarTextPacket(new TextComponent("Selected" + mode + "Slot: " + readableSlot + " (" + selectedSealName +")")));
                });
            }
            else {
                pPlayer.getItemInHand(pUsedHand).getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
                    if (wand.getActiveSeal() != null) {
                        wand.setActiveSeal(null);
                    }
                    else if (wand.getActiveSeal() == null && wand.isSelectedIsAttack()) {
                        int attackSelectedSlot = wand.getSelectedSealSlot() - tier.getPassiveSlots();
                        if (attackSelectedSlot >= 1) {
                            if (wand.getAttackSeals().size() != 0 && wand.getAttackSeals().get(attackSelectedSlot - 1) != null) {
                                wand.setActiveSeal(wand.getAttackSeals().get(attackSelectedSlot  - 1));
                            }
                        }
                    }
                    else if (wand.getActiveSeal() == null && !wand.isSelectedIsAttack()){
                        if (wand.getPassiveSeals().get(wand.getSelectedSealSlot() - 1) != null) {
                            wand.setActiveSeal(wand.getPassiveSeals().get(wand.getSelectedSealSlot() - 1));
                        }
                    }
                });
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    @SuppressWarnings("all")
    public InteractionResult useOn(UseOnContext pContext) {
        if (hasSeal("hoeing", pContext.getItemInHand())) {
            Items.DIAMOND_HOE.useOn(pContext);
        }
        if (hasSeal("axing", pContext.getItemInHand())) {
            Items.DIAMOND_AXE.useOn(pContext);
        }
        return super.useOn(pContext);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        this.stack = pStack;
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (isCorrectToolForDrops(pStack, pState)) {
            return hasSeal("mining_speed", pStack) ? this.speed * 2: this.speed;
        }
        return 1.0f;
    }

    public boolean hasSeal(String sealID, ItemStack stack) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
            if (wand.getAttackSeals().contains(SealRegistry.allSeals.get(sealID)) || wand.getPassiveSeals().contains(SealRegistry.allSeals.get(sealID)) || wand.getAmplifyingSeals().contains(SealRegistry.allSeals.get(sealID))) {
                toReturn.set(true);
            }
        });
        return toReturn.get();
    }

    @Override
    public float getAttackDamage() {
        if (stack != null) {
            if (hasSeal("swording", stack)) {
                return attackDamageBaseline * 2;
            }
        }
        return super.getAttackDamage();
    }

    public boolean canAddSeal(ItemStack stack, int type) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        if (type == 0) {
            int passiveSealSlots = tier.getPassiveSlots();
            stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
                toReturn.set(wand.getPassiveSeals().size() < passiveSealSlots);
            });
        }
        else if (type == 1) {
            int attackSealSlots = tier.getAttackSlots();
            stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
                toReturn.set(wand.getAttackSeals().size() < attackSealSlots);
            });
        }
        else if (type == 2) {
            int amplifyingSealSlots = tier.getAmplificationSlots();
            stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
                toReturn.set(wand.getAmplifyingSeals().size() < amplifyingSealSlots);
            });
        }
        return toReturn.get();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
        tooltip.add(new TextComponent("Passive Seals:"));
        pStack.getCapability(ItemWandProvider.WAND).ifPresent(itemWand -> {
            int a = 0;
            for (ISeal seal: itemWand.getPassiveSeals()) {
                tooltip.add(new TextComponent("    " + capitalizeString(seal.getId())).withStyle(ChatFormatting.AQUA));
                a++;
            }
            if (a <= tier.getPassiveSlots()) {
                for (int i = a; i < tier.getPassiveSlots(); i++) {
                    tooltip.add(new TextComponent("    Slot " + (i + 1) + " is empty").withStyle(ChatFormatting.DARK_GREEN));
                }
            }
            tooltip.add(new TextComponent("Offensive/Defensive Seals:"));
            int b = 0;
            for (ISeal seal: itemWand.getAttackSeals()) {
                tooltip.add(new TextComponent("    " + capitalizeString(seal.getId())).withStyle(ChatFormatting.RED));
                b++;
            }
            if (b <= tier.getAttackSlots()) {
                for (int i = b; i < tier.getAttackSlots(); i++) {
                    tooltip.add(new TextComponent("    Slot " + (i + 1) + " is empty").withStyle(ChatFormatting.DARK_GREEN));
                }
            }
            tooltip.add(new TextComponent("Amplifying Seals:"));
            int c = 0;
            for (ISeal seal: itemWand.getAmplifyingSeals()) {
                tooltip.add(new TextComponent("    " + capitalizeString(seal.getId())).withStyle(ChatFormatting.GOLD));
                c++;
            }
            if (c <= tier.getAmplificationSlots()) {
                for (int i = c; i < tier.getAmplificationSlots(); i++) {
                    tooltip.add(new TextComponent("    Slot " + (i + 1) + " is empty").withStyle(ChatFormatting.DARK_GREEN));
                }
            }
        });
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if ('_' == chars[i]) {
                chars[i] = ' ';
            } else if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
