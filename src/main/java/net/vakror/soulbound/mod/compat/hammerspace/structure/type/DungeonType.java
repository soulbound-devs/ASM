package net.vakror.soulbound.mod.compat.hammerspace.structure.type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.vakror.soulbound.mod.SoulboundMod;

import java.util.List;
import java.util.Random;

import static net.vakror.soulbound.mod.compat.hammerspace.structure.type.DefaultDungeonTypes.ALL_DUNGEON_TYPES;

public interface DungeonType {
    ResourceLocation getFile(int size, int layer, RandomSource randomSource);

    static DungeonType getRandomType() {
        return ALL_DUNGEON_TYPES.get(new Random().nextInt(ALL_DUNGEON_TYPES.size()));
    }


    static DungeonType getTypeFromId(String id) {
        List<DungeonType> types = ALL_DUNGEON_TYPES.stream().filter((type) -> type.id().equals(id)).toList();
        if (types.size() > 1) {
            throw new IllegalStateException(types.size() + " Dungeon Types Have The ID " + id + "!");
        } else if (types.isEmpty()) {
            SoulboundMod.LOGGER.warn("Attempted to look up dungeon type for unknown ID: " + id);
        } else {
            return types.get(0);
        }

        return DefaultDungeonTypes.DEFAULT;
    }


    String id();

    StructurePieceType structurePiece();

    List<ProcessorRule> rules();
}
