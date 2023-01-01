package net.vakror.soulbound.items.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.seals.Seal;
import net.vakror.soulbound.wand.ItemWandProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.item.HoeItem.changeIntoState;

public class WandItem extends DiggerItem {

    public WandItem(Properties properties) {
        super(3, -3, Tiers.DIAMOND, BlockTags.create(new ResourceLocation(SoulboundMod.MOD_ID, "none")), properties);
    }

//    @Nullable
//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
//        return new ItemWandProvider();
//    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        assert Minecraft.getInstance().player != null;
        if (TierSortingRegistry.isCorrectTierForDrops(getTier(), state)) {
            if (!Minecraft.getInstance().player.level.isClientSide()) {
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
        }
        return false;
    }

    @Override
    @SuppressWarnings("all")
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            if (hasSeal("hoeing", pContext.getItemInHand())) {
                int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(pContext);
                if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
                Level level = pContext.getLevel();
                BlockPos blockpos = pContext.getClickedPos();
                BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.HOE_TILL, false);
                Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
                if (pair == null) {
                    return InteractionResult.PASS;
                } else {
                    Predicate<UseOnContext> predicate = pair.getFirst();
                    Consumer<UseOnContext> consumer = pair.getSecond();
                    if (predicate.test(pContext)) {
                        Player player = pContext.getPlayer();
                        level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!level.isClientSide) {
                            consumer.accept(pContext);
                            if (player != null) {
                                pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                                    p_150845_.broadcastBreakEvent(pContext.getHand());
                                });
                            }
                        }

                        return InteractionResult.sidedSuccess(level.isClientSide);
                    } else {
                        return InteractionResult.PASS;
                    }
                }
            } else if (hasSeal("axing", pContext.getItemInHand())) {
            }
        }
        return super.useOn(pContext);
    }

    public boolean hasSeal(String sealID, ItemStack stack) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
            if (wand.getSeals().contains(SealRegistry.seals.get(sealID))) {
                toReturn.set(true);
            }
        });
        return toReturn.get();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
        tooltip.add(new TextComponent("Seals:"));
        pStack.getCapability(ItemWandProvider.WAND).ifPresent(itemWand -> {
            for (ISeal seal: itemWand.getSeals()) {
                tooltip.add(new TextComponent("    " + capitalizeString(seal.getId())));
            }
        });
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
