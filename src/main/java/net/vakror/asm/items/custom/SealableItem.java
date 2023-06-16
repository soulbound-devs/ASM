package net.vakror.asm.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.vakror.asm.ASMMod;
import net.vakror.asm.seal.tier.ISealableTier;
import net.vakror.asm.capability.wand.ItemSeal;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.items.custom.seals.SealItem;
import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.SealRegistry;
import net.vakror.asm.seal.SealType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SealableItem extends DiggerItem {

    protected final ISealableTier tier;
    protected ItemStack stack;

    public SealableItem(Properties properties, ISealableTier tier) {
        super(3, -3, Tiers.DIAMOND, BlockTags.create(new ResourceLocation(ASMMod.MOD_ID, "none")), properties);
        this.tier = tier;
    }

    public boolean hasSeal(String sealID, ItemStack stack) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            if (wand.getAttackSeals().contains(SealRegistry.allSeals.get(sealID)) || wand.getPassiveSeals().contains(SealRegistry.allSeals.get(sealID)) || wand.getAmplifyingSeals().contains(SealRegistry.allSeals.get(sealID))) {
                toReturn.set(true);
            }
        });
        return toReturn.get();
    }

    public boolean hasSealWithProperty(String propertyId) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            wand.getAllActivatableSeals().forEach((seal -> {
                if (seal.properties().contains(new SealProperty(propertyId))) {
                    toReturn.set(true);
                }
            }));
        });
        return toReturn.get();
    }

    public List<ISeal> getAllSealsWithProperty(String propertyId) {
        List<ISeal> seals = new ArrayList<>();
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            wand.getAllActivatableSeals().forEach((seal -> {
                if (seal.properties().contains(new SealProperty(propertyId))) {
                    seals.add(seal);
                }
            }));
        });
        return seals;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int p_41407_, boolean p_41408_) {
        this.stack = pStack;
        super.inventoryTick(pStack, pLevel, pEntity, p_41407_, p_41408_);
    }

    public boolean canAddSeal(ItemStack sealable, SealType type, ItemStack sealItem) {
        return switch(type) {
            case PASSIVE -> canAddPassiveSeal(sealable, sealItem);
            case OFFENSIVE -> canAddOffensiveSeal(sealable, sealItem);
            case AMPLIFYING -> canAddAmplifyingSeal(sealable, sealItem);
        };
    }

    private boolean canAddPassiveSeal(ItemStack sealable, ItemStack sealItem) {
        AtomicBoolean toReturn = new AtomicBoolean();
        int passiveSlots = tier.getPassiveSlots();
        sealable.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            String id = ((SealItem) sealItem.getItem()).getId();
            if (!((SealItem) sealItem.getItem()).canAddMultiple()) {
                if (!wand.getPassiveSeals().contains(SealRegistry.passiveSeals.get(id))) {
                    toReturn.set(wand.getPassiveSeals().size() < passiveSlots);
                }
            } else {
                if (wand.getAmountOfTimesThatSealIsPresent(SealType.PASSIVE, ((SealItem) sealItem.getItem()).getId()) < ((SealItem) sealItem.getItem()).getMaxSealStack()) {
                    toReturn.set(wand.getPassiveSeals().size() < passiveSlots);
                }
            }
        });
        return toReturn.get();
    }

    private boolean canAddOffensiveSeal(ItemStack sealable, ItemStack sealItem) {
        AtomicBoolean toReturn = new AtomicBoolean();
        int offensiveSlots = tier.getAttackSlots();
        sealable.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            String id = ((SealItem) sealItem.getItem()).getId();
            if (!((SealItem) sealItem.getItem()).canAddMultiple()) {
                if (!wand.getAttackSeals().contains(SealRegistry.attackSeals.get(id))) {
                    toReturn.set(wand.getAttackSeals().size() < offensiveSlots);
                }
            } else {
                if (wand.getAmountOfTimesThatSealIsPresent(SealType.OFFENSIVE, ((SealItem) sealItem.getItem()).getId()) < ((SealItem) sealItem.getItem()).getMaxSealStack()) {
                    toReturn.set(wand.getAttackSeals().size() < offensiveSlots);
                }
            }
        });
        return toReturn.get();
    }

    private boolean canAddAmplifyingSeal(ItemStack sealable, ItemStack sealItem) {
        AtomicBoolean toReturn = new AtomicBoolean();
        int amplifyingSealSlots = tier.getAmplificationSlots();
        sealable.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            String id = ((SealItem) sealItem.getItem()).getId();
            if (!((SealItem) sealItem.getItem()).canAddMultiple()) {
                if (!wand.getAmplifyingSeals().contains(SealRegistry.amplifyingSeals.get(id))) {
                    toReturn.set(wand.getAmplifyingSeals().size() < amplifyingSealSlots);
                }
            } else {
                if (wand.getAmountOfTimesThatSealIsPresent(SealType.AMPLIFYING, ((SealItem) sealItem.getItem()).getId()) < ((SealItem) sealItem.getItem()).getMaxSealStack()) {
                    toReturn.set(wand.getAmplifyingSeals().size() < amplifyingSealSlots);
                }
            }
        });
        return toReturn.get();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
        pStack.getCapability(ItemSealProvider.SEAL).ifPresent(itemSeal -> {
            ISeal activeSeal = itemSeal.getActiveSeal();
            if (tier.getPassiveSlots() != 0) {
                addPassiveSealTooltips(itemSeal, tooltip, activeSeal);
            }
            if (tier.getAttackSlots() != 0) {
                addOffensiveSealTooltips(itemSeal, tooltip, activeSeal);
            }
            if (tier.getAmplificationSlots() != 0) {
                addAmplifyingSealTooltip(itemSeal, tooltip);
            }
        });
    }

    private void addPassiveSealTooltips(ItemSeal itemSeal, List<Component> tooltip, ISeal activeSeal) {
        tooltip.add(Component.literal("Passive Seals:"));
        int count = 0;
        for (ISeal seal: itemSeal.getPassiveSeals()) {
            String active = "    ";
            if (activeSeal != null) {
                active = (activeSeal.getId().equals(seal.getId())) ? activeCharacter(): "";
            }
            Component stylizedTooltip = stylizeSeal(Component.literal(buildTooltipString(SealType.AMPLIFYING, seal, itemSeal, active)), ChatFormatting.AQUA);
            tooltip.add(stylizedTooltip);
            count++;
        }
        if (count <= tier.getPassiveSlots()) {
            for (int slot = count; slot < tier.getPassiveSlots(); slot++) {
                tooltip.add(Component.literal("    Slot " + (slot + 1) + " is empty").withStyle(ChatFormatting.DARK_GREEN));
            }
        }
    }

    private void addOffensiveSealTooltips(ItemSeal itemSeal, List<Component> tooltip, ISeal activeSeal) {
        tooltip.add(Component.literal("Offensive/Defensive Seals:"));
        int count = 0;
        for (ISeal seal: itemSeal.getAttackSeals()) {
            String active = "   ";
            if (activeSeal != null) {
                active = (activeSeal.getId().equals(seal.getId())) ? activeCharacter(): "";
            }
            Component stylizedTooltip = stylizeSeal(Component.literal(buildTooltipString(SealType.AMPLIFYING, seal, itemSeal, active)), ChatFormatting.RED);
            tooltip.add(stylizedTooltip);
            count++;
        }
        if (count <= tier.getAttackSlots()) {
            for (int slot = count; slot < tier.getAttackSlots(); slot++) {
                tooltip.add(Component.literal("    Slot " + (slot + 1) + " is empty").withStyle(ChatFormatting.DARK_GREEN));
            }
        }
    }

    private void addAmplifyingSealTooltip(ItemSeal itemSeal, List<Component> tooltip) {
        tooltip.add(Component.literal("Amplifying Seals:"));
        int count = 0;
        for (ISeal seal: itemSeal.getAmplifyingSeals()) {
            tooltip.add(Component.literal(buildTooltipString(SealType.AMPLIFYING, seal, itemSeal, "")).withStyle(ChatFormatting.GOLD));
            count++;
        }
        if (count <= tier.getAmplificationSlots()) {
            for (int slot = count; slot < tier.getAmplificationSlots(); slot++) {
                tooltip.add(Component.literal("    Slot " + (slot + 1) + " is empty").withStyle(ChatFormatting.DARK_GREEN));
            }
        }
    }

    private String buildTooltipString(SealType sealType, ISeal seal, ItemSeal itemSeal, String active) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  ");
        stringBuilder.append(active);
        stringBuilder.append(" ");
        stringBuilder.append(capitalizeString(seal.getId()));
        return stringBuilder.toString();
    }

    private static Component stylizeSeal(MutableComponent seal, ChatFormatting color) {
        return seal.withStyle(new Style(TextColor.fromLegacyFormat(color),
                false, // bold
                false, // italic
                false, // underlined
                false, // strikethrough
                false, // obfuscated
                null, // click event
                null, // hover event
                null, // insertion
                new ResourceLocation(ASMMod.MOD_ID, "wand") // font
        ));
    }

    public static String activeCharacter() {
        return "\uEff1";
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
