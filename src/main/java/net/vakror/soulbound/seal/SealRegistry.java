package net.vakror.soulbound.seal;


import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> allSeals = new HashMap<>();
    public static Map<String, ISeal> passiveSeals = new HashMap<>();
    public static Map<String, ISeal> attackSeals = new HashMap<>();
    public static Map<String, ISeal> amplifyingSeals = new HashMap<>();
    public static Map<String, DeferredHolder<Item>> sealItems = new HashMap<>();

    public static void addSeal(ISeal seal, SealType type) {
        switch (type) {
            case PASSIVE -> passiveSeals.put(seal.getId(), seal);
            case OFFENSIVE -> attackSeals.put(seal.getId(), seal);
            case AMPLIFYING -> amplifyingSeals.put(seal.getId(), seal);
        }
        allSeals.put(seal.getId(), seal);
    }

    public static void addAttackSeal(ISeal seal, DeferredHolder<Item> sealItem) {
        attackSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItems.put(seal.getId(), sealItem);
    }

    public static void addPassiveSeal(ISeal seal, DeferredHolder<Item> sealItem) {
        passiveSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItems.put(seal.getId(), sealItem);
    }

    public static void addAmplifyingSealSeal(ISeal seal, DeferredHolder<Item> sealItem) {
        amplifyingSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItems.put(seal.getId(), sealItem);
    }

    /**
     * Called when done registering. Makes all maps immutable.
     */
    public static void doneRegistering() {
        allSeals = ImmutableMap.copyOf(allSeals);
        passiveSeals = ImmutableMap.copyOf(passiveSeals);
        attackSeals = ImmutableMap.copyOf(attackSeals);
        amplifyingSeals = ImmutableMap.copyOf(amplifyingSeals);
        sealItems = ImmutableMap.copyOf(sealItems);
    }
}