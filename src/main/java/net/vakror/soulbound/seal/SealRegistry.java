package net.vakror.soulbound.seal;


import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.seal.seals.activatable.SwordSeal;
import net.vakror.soulbound.seal.seals.activatable.tool.AxingSeal;
import net.vakror.soulbound.seal.seals.activatable.tool.HoeingSeal;
import net.vakror.soulbound.seal.seals.activatable.tool.PickaxingSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.ColumnUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.PickupSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.RowUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.StackSizeUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.wand.haste.HasteSeal;
import net.vakror.soulbound.util.ArithmeticActionType;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> allSeals = new HashMap<>();
    public static Map<String, ISeal> passiveSeals = new HashMap<>();
    public static Map<String, ISeal> attackSeals = new HashMap<>();
    public static Map<String, ISeal> amplifyingSeals = new HashMap<>();
    public static Map<String, RegistryObject<Item>> sealItemGetter = new HashMap<>();

    public static void registerSeals() {
        addAttackSeal(new AxingSeal(), ModItems.AXING_SEAL);
        addPassiveSeal(new PickaxingSeal(), ModItems.PICKAXING_SEAL);
        addPassiveSeal(new HoeingSeal(), ModItems.HOEING_SEAL);
        addAmplifyingSealSeal(new HasteSeal.HasteSealTierOne(), ModItems.MINING_SPEED_SEAL_TIER_1);
        addAmplifyingSealSeal(new HasteSeal.HasteSealTierTwo(), ModItems.MINING_SPEED_SEAL_TIER_2);
        addAmplifyingSealSeal(new HasteSeal.HasteSealTierThree(), ModItems.MINING_SPEED_SEAL_TIER_3);
        addAmplifyingSealSeal(new PickupSeal(), ModItems.SACK_PICKUP_SEAL);
        addAmplifyingSealSeal(new StackSizeUpgradeSeal(1, 2, ArithmeticActionType.MULTIPLY), ModItems.SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1);
        addAmplifyingSealSeal(new ColumnUpgradeSeal(1, 2, ArithmeticActionType.ADD), ModItems.SACK_COLUMN_UPGRADE_SEAL_TIER_1);
        addAmplifyingSealSeal(new RowUpgradeSeal(1, 2, ArithmeticActionType.ADD), ModItems.SACK_ROW_UPGRADE_SEAL_TIER_1);
        addAttackSeal(new SwordSeal(), ModItems.SWORDING_SEAL);
    }

    public static void addSeal(ISeal seal, SealType type) {
        switch (type) {
            case PASSIVE -> passiveSeals.put(seal.getId(), seal);
            case OFFENSIVE -> attackSeals.put(seal.getId(), seal);
            case AMPLIFYING -> amplifyingSeals.put(seal.getId(), seal);
        }
        allSeals.put(seal.getId(), seal);
    }

    public static void addAttackSeal(ISeal seal, RegistryObject<Item> sealItem) {
        attackSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItemGetter.put(seal.getId(), sealItem);
    }

    public static void addPassiveSeal(ISeal seal, RegistryObject<Item> sealItem) {
        passiveSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItemGetter.put(seal.getId(), sealItem);
    }

    public static void addAmplifyingSealSeal(ISeal seal, RegistryObject<Item> sealItem) {
        amplifyingSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItemGetter.put(seal.getId(), sealItem);
    }
}