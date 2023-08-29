package net.vakror.soulbound.mod.compat.hammerspace.structure.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.vakror.soulbound.mod.SoulboundMod;

import java.util.List;

public class DungeonUtil {
    public static BlockPos getGenerationPoint(ResourceLocation nbtFile, int y) {
        List<Pair<Integer, Integer>> offsets = DungeonSpawnPointUtils.SPAWN_LOCATIONS.get(nbtFile).stream().toList();
        if (!offsets.isEmpty()) {
            int xOffset;
            int zOffset;
            if (offsets.size() > 1) {
                Pair<Integer, Integer> finalOffsets = offsets.get(RandomSource.create().nextInt(offsets.size()));
                xOffset = finalOffsets.getFirst();
                zOffset = finalOffsets.getSecond();
            } else {
                xOffset = offsets.get(0).getFirst();
                zOffset = offsets.get(0).getSecond();
            }
            if (nbtFile.equals(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"))) {
                xOffset += 17;
                zOffset += 17;
            }
            System.out.println("spawn: " + xOffset + ", " + zOffset);
            return new BlockPos(-25 + xOffset, y, -25 + zOffset);
        }
        return BlockPos.ZERO;
    }
}