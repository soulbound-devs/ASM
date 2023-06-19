package net.vakror.asm.seal;


import net.vakror.asm.seal.seals.activatable.SwordSeal;
import net.vakror.asm.seal.seals.activatable.tool.AxingSeal;
import net.vakror.asm.seal.seals.activatable.tool.HoeingSeal;
import net.vakror.asm.seal.seals.activatable.tool.PickaxingSeal;
import net.vakror.asm.seal.seals.amplifying.sack.ColumnUpgradeSeal;
import net.vakror.asm.seal.seals.amplifying.sack.PickupSeal;
import net.vakror.asm.seal.seals.amplifying.sack.RowUpgradeSeal;
import net.vakror.asm.seal.seals.amplifying.sack.StackSizeUpgradeSeal;
import net.vakror.asm.seal.seals.amplifying.wand.haste.HasteSeal;
import net.vakror.asm.util.ArithmeticActionType;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> allSeals = new HashMap<>();
    public static Map<String, ISeal> passiveSeals = new HashMap<>();
    public static Map<String, ISeal> attackSeals = new HashMap<>();
    public static Map<String, ISeal> amplifyingSeals = new HashMap<>();

    public static void registerSeals() {
        addAttackSeal(new AxingSeal());
        addPassiveSeal(new PickaxingSeal());
        addPassiveSeal(new HoeingSeal());
        addAmplifyingSealSeal(new HasteSeal.HasteSealTierOne());
        addAmplifyingSealSeal(new HasteSeal.HasteSealTierTwo());
        addAmplifyingSealSeal(new HasteSeal.HasteSealTierThree());
        addAmplifyingSealSeal(new PickupSeal());
        addAmplifyingSealSeal(new StackSizeUpgradeSeal(1, 2, ArithmeticActionType.MULTIPLY));
        addAmplifyingSealSeal(new ColumnUpgradeSeal(1, 2, ArithmeticActionType.ADD));
        addAmplifyingSealSeal(new RowUpgradeSeal(1, 2, ArithmeticActionType.ADD));
        addAttackSeal(new SwordSeal());
    }

    public static void addSeal(ISeal seal, SealType type) {
        switch (type) {
            case PASSIVE -> passiveSeals.put(seal.getId(), seal);
            case OFFENSIVE -> attackSeals.put(seal.getId(), seal);
            case AMPLIFYING -> amplifyingSeals.put(seal.getId(), seal);
        }
        allSeals.put(seal.getId(), seal);
    }

    public static void addAttackSeal(ISeal seal) {
        attackSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);

    }

    public static void addPassiveSeal(ISeal seal) {
        passiveSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
    }

    public static void addAmplifyingSealSeal(ISeal seal) {
        amplifyingSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
    }
}