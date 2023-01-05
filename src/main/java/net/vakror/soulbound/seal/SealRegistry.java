package net.vakror.soulbound.seal;


import net.vakror.soulbound.seal.seals.Seal;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> allSeals = new HashMap<>();
    public static Map<String, ISeal> passiveSeals = new HashMap<>();
    public static Map<String, ISeal> attackSeals = new HashMap<>();
    public static Map<String, ISeal> amplifyingSeals = new HashMap<>();

    public static void registerSeals() {
        addAttackSeal("axing", new Seal("axing"));
        addPassiveSeal("pickaxing", new Seal("pickaxing"));
        addPassiveSeal("hoeing", new Seal("hoeing"));
        addAmplifyingSealSeal("mining_speed", new Seal("mining_speed"));
        addAttackSeal("swording", new Seal("swording"));
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
