package net.vakror.asm.seal;


import net.vakror.asm.seal.seals.*;
import net.vakror.asm.seal.type.BaseSeal;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> allSeals = new HashMap<>();
    public static Map<String, ISeal> passiveSeals = new HashMap<>();
    public static Map<String, ISeal> attackSeals = new HashMap<>();
    public static Map<String, ISeal> amplifyingSeals = new HashMap<>();

    public static void registerSeals() {
        addAttackSeal("axing", new AxingSeal());
        addPassiveSeal("pickaxing", new PickaxingSeal());
        addPassiveSeal("hoeing", new HoeingSeal());
        addAmplifyingSealSeal("mining_speed", new HasteSeal());
        addAttackSeal("swording", new SwordSeal());
    }

    public static void addPassiveSeal(String name, ISeal seal) {
        passiveSeals.put(name, seal);
        allSeals.put(name, seal);
    }

    public static void addAttackSeal(String name, ISeal seal) {
        attackSeals.put(name, seal);
        allSeals.put(name, seal);
    }

    public static void addAmplifyingSealSeal(String name, ISeal seal) {
        amplifyingSeals.put(name, seal);
        allSeals.put(name, seal);
    }
}
