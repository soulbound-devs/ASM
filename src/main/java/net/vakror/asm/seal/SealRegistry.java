package net.vakror.asm.seal;


import net.vakror.asm.seal.seals.activatable.AxingSeal;
import net.vakror.asm.seal.seals.activatable.HoeingSeal;
import net.vakror.asm.seal.seals.activatable.PickaxingSeal;
import net.vakror.asm.seal.seals.activatable.SwordSeal;
import net.vakror.asm.seal.seals.amplifying.MiningSpeedSeal;

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
        addAmplifyingSealSeal(new MiningSpeedSeal());
        addAttackSeal(new SwordSeal());
    }

    public static void addPassiveSeal(ISeal seal) {
        passiveSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
    }

    public static void addSeal(ISeal seal, SealType type) {
        switch (type) {
            case PASSIVE ->  passiveSeals.put(seal.getId(), seal);
            case OFFENSIVE ->  attackSeals.put(seal.getId(), seal);
            case AMPLIFYING ->  amplifyingSeals.put(seal.getId(), seal);
        }
        allSeals.put(seal.getId(), seal);
    }

    public static void addAttackSeal(ISeal seal) {
        attackSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
    }

    public static void addAmplifyingSealSeal(ISeal seal) {
        amplifyingSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
    }
}
