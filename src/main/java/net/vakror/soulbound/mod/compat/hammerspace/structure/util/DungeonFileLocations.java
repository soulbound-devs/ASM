package net.vakror.soulbound.mod.compat.hammerspace.structure.util;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DungeonFileLocations {
    public static Multimap<Pair<Integer, Integer>, ResourceLocation> FILES = LinkedListMultimap.create();

    public static List<ResourceLocation> getFiles(Pair<Integer, Integer> sizeAndLayer) {
        return FILES.get(sizeAndLayer).stream().toList();
    }
}
