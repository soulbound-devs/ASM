package net.vakror.soulbound.mod.compat.hammerspace.structure.type;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.mod.compat.hammerspace.structure.util.DungeonFileLocations;
import net.vakror.soulbound.mod.compat.hammerspace.structure.util.DungeonProcessorRules;
import net.vakror.soulbound.mod.compat.hammerspace.structure.piece.ModDungeonPieces;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public enum DefaultDungeonTypes implements DungeonType {
    DEFAULT("soulbound:default", new ArrayList<>(), ModDungeonPieces.DEFAULT_DUNGEON_PIECE),
    DARK_CREEPY("soulbound:dark_creepy", DungeonProcessorRules.CREEPY_DEEPSLATE, ModDungeonPieces.DEFAULT_DUNGEON_PIECE),
    DEEP_BLOOD("soulbound:deep_blood", DungeonProcessorRules.DEEP_BLOOD, ModDungeonPieces.DEFAULT_DUNGEON_PIECE),
    OLD_RUINS("soulbound:old_ruins", DungeonProcessorRules.OLD_RUINS, ModDungeonPieces.DEFAULT_DUNGEON_PIECE),
    AMETHYST_VOID("soulbound:amethyst_void", DungeonProcessorRules.AMETHYST_VOID, ModDungeonPieces.DEFAULT_DUNGEON_PIECE),
    GILDED_HOARD("soulbound:gilded_hoard", DungeonProcessorRules.GILDED_HOARD, ModDungeonPieces.DEFAULT_DUNGEON_PIECE),
    ANCIENT_ENDER("soulbound:ancient_ender", DungeonProcessorRules.ANCIENT_ENDER, ModDungeonPieces.DEFAULT_DUNGEON_PIECE);

    final String name;
    List<ProcessorRule> rules;
    final RegistryObject<StructurePieceType> structurePiece;

    public static List<DungeonType> ALL_DUNGEON_TYPES = new ArrayList<>();

    DefaultDungeonTypes(String name, List<ProcessorRule> rules, RegistryObject<StructurePieceType> structurePiece) {
        this.name = name;
        this.rules = rules;
        this.structurePiece = structurePiece;
    }

    @Override
    public String id() {
        return this.name;
    }

    @Override
    public StructurePieceType structurePiece() {
        return structurePiece.get();
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }

    public void setRules(List<ProcessorRule> rules) {
        this.rules = rules;
    }

    @Override
    public List<ProcessorRule> rules() {
        if (rules == null) {
            DungeonProcessorRules.init();
        }
        return rules;
    }

    @Override
    public ResourceLocation getFile(int size, int layer, RandomSource randomSource) {
        List<ResourceLocation> files = DungeonFileLocations.getFiles(new Pair<>(size, layer));
        if (!files.isEmpty()) {
            if (files.size() == 1) {
                return files.get(0);
            } else {
                return files.get(randomSource.nextInt(files.size()));
            }
        }
        return null;
    }
}
