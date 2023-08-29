package net.vakror.soulbound.mod.compat.hammerspace.structure.util;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.mod.SoulboundMod;

import java.util.List;

public class DungeonFileLocations {
    private static Multimap<Pair<Integer, Integer>, ResourceLocation> FILES = null;

    public static void init() {
        ImmutableMultimap.Builder<Pair<Integer, Integer>, ResourceLocation> fileBuilder = new ImmutableMultimap.Builder<>();
        fileBuilder.put(new Pair<>(50, 0), new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"));
        fileBuilder.put(new Pair<>(50, 0), new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"));
        FILES = fileBuilder.build();
    }

    public static List<ResourceLocation> getFiles(Pair<Integer, Integer> sizeAndLayer) {
        return FILES.get(sizeAndLayer).stream().toList();
    }
}
