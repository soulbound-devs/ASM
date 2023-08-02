package net.vakror.soulbound.compat.hammerspace.structure.util;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;

public class DungeonSpawnPointUtils {
    public static Multimap<ResourceLocation, Pair<Integer, Integer>> SPAWN_LOCATIONS;

    public static void init() {
        ImmutableMultimap.Builder<ResourceLocation, Pair<Integer, Integer>> locationBuilder = new ImmutableMultimap.Builder<>();
        locationBuilder
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-20, -15))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-14, -7))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-32, -32))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-23, -40))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-18, -2))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-33, -5))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-4, -37))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-11, -24))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), new Pair<>(-42, -19));

        locationBuilder
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-24, -24))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(24, -24))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-24, 24))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(24, 24))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-23, 17))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-14, -5))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-7, -11))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-19, -24))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-16, -20))
                .put(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), new Pair<>(-20, -8));

        SPAWN_LOCATIONS = locationBuilder.build();
    }
}
