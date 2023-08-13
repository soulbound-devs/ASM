package net.vakror.soulbound.compat.hammerspace.structure.type;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.vakror.soulbound.compat.hammerspace.structure.util.DungeonFileLocations;
import net.vakror.soulbound.compat.hammerspace.structure.util.DungeonProcessorRules;
import net.vakror.soulbound.compat.hammerspace.structure.piece.ModDungeonPieces;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public enum DungeonType {
    DEFAULT("default", 0, new ArrayList<>(), ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get()),
    DARK_CREEPY("dark_creepy", 1, DungeonProcessorRules.CREEPY_DEEPSLATE, ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get()),
    DEEP_BLOOD("deep_blood", 2, DungeonProcessorRules.DEEP_BLOOD, ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get()),
    OLD_RUINS("old_ruins", 3, DungeonProcessorRules.OLD_RUINS, ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get()),
    AMETHYST_VOID("amethyst_void", 4, DungeonProcessorRules.AMETHYST_VOID, ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get()),
    GILDED_HOARD("gilded_hoard", 5, DungeonProcessorRules.GILDED_HOARD, ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get()),
    ANCIENT_ENDER("ancient_ender", 6, DungeonProcessorRules.ANCIENT_ENDER, ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get());

    String name;
    int index;
    List<ProcessorRule> rules;
    StructurePieceType structurePiece;

    private DungeonType(String name, int index, List<ProcessorRule> rules, StructurePieceType structurePiece) {
        this.name = name;
        this.index = index;
        this.rules = rules;
        this.structurePiece = structurePiece;
    }

    public String id() {
        return this.name;
    }

    public StructurePieceType structurePiece() {
        return structurePiece;
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }

    public List<ProcessorRule> rules() {
        if (rules == null) {
            DungeonProcessorRules.init();
        }
        return rules;
    }

    public int getIndex() {
        return this.index;
    }

    public static DungeonType getTypeFromIndex(int index) {
        DungeonType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            DungeonType theme = var1[var3];
            if (theme.getIndex() == index) {
                return theme;
            }
        }

        return DEFAULT;
    }

    public static DungeonType getRandomType() {
        int rand = (new Random()).nextInt(values().length);
        while (rand == 0) {
            rand = new Random().nextInt(values().length);
        }

        for (DungeonType theme : values()) {
            if (theme.getIndex() == rand) {
                return theme;
            }
        }

        throw new IllegalStateException("Random theme failed: index = " + rand);
    }

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
