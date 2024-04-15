package net.vakror.soulbound.seal;


import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.items.custom.seals.SealItem;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<ResourceLocation, ISeal> allSeals = new HashMap<>();
    public static Map<ResourceLocation, ISeal> passiveSeals = new HashMap<>();
    public static Map<ResourceLocation, ISeal> attackSeals = new HashMap<>();
    public static Map<ResourceLocation, ISeal> amplifyingSeals = new HashMap<>();
    public static Map<ResourceLocation, DeferredHolder<Item, SealItem>> sealItems = new HashMap<>();

    public static void addSeal(ISeal seal, SealType type) {
        switch (type) {
            case PASSIVE -> passiveSeals.put(seal.getId(), seal);
            case OFFENSIVE -> attackSeals.put(seal.getId(), seal);
            case AMPLIFYING -> amplifyingSeals.put(seal.getId(), seal);
        }
        allSeals.put(seal.getId(), seal);
    }

    public static void addAttackSeal(ISeal seal, DeferredHolder<Item, SealItem> sealItem) {
        attackSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItems.put(seal.getId(), sealItem);
    }

    public static void addPassiveSeal(ISeal seal, DeferredHolder<Item, SealItem> sealItem) {
        passiveSeals.put(seal.getId(), seal);
        allSeals.put(seal.getId(), seal);
        sealItems.put(seal.getId(), sealItem);
    }

    public static void addAmplifyingSealSeal(ISeal seal, DeferredHolder<Item, SealItem> sealItem) {
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