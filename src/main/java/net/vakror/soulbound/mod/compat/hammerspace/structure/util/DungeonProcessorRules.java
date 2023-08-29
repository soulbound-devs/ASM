package net.vakror.soulbound.mod.compat.hammerspace.structure.util;

import com.github.L_Ender.cataclysm.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.vakror.soulbound.mod.compat.hammerspace.blocks.ModDungeonBlocks;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DungeonType;

import java.util.List;

public class DungeonProcessorRules {
    public static List<ProcessorRule> CREEPY_DEEPSLATE;
    public static List<ProcessorRule> DEEP_BLOOD;
    public static List<ProcessorRule> OLD_RUINS;
    public static List<ProcessorRule> AMETHYST_VOID;
    public static List<ProcessorRule> GILDED_HOARD;
    public static List<ProcessorRule> ANCIENT_ENDER;

    public static void init() {
        CREEPY_DEEPSLATE = new ImmutableList.Builder<ProcessorRule>()
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE_TILES.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.GILDED_BLACKSTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.POLISHED_BLACKSTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.CHISELED_POLISHED_BLACKSTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.BLACKSTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.BLACK_CONCRETE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.BASALT.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.POLISHED_BASALT.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.SMOOTH_BASALT.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, Blocks.COBBLED_DEEPSLATE.defaultBlockState())).build();

        DEEP_BLOOD = new ImmutableList.Builder<ProcessorRule>()
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.05f), AlwaysTrueTest.INSTANCE, Blocks.STRIPPED_MANGROVE_WOOD.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.05f), AlwaysTrueTest.INSTANCE, Blocks.MANGROVE_PLANKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.RED_NETHER_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.CRIMSON_HYPHAE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.NETHERRACK.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.05f), AlwaysTrueTest.INSTANCE, Blocks.RED_TERRACOTTA.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.05f), AlwaysTrueTest.INSTANCE, Blocks.RED_CONCRETE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.REDSTONE_BLOCK.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, Blocks.NETHER_WART_BLOCK.defaultBlockState())).build();

        OLD_RUINS = new ImmutableList.Builder<ProcessorRule>()
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.STONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.CHISELED_STONE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.POLISHED_DEEPSLATE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.POLISHED_ANDESITE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.CHISELED_STONE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.POLISHED_ANDESITE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_STONE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, ModBlocks.CHISELED_STONE_BRICK_PILLAR.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, Blocks.COBBLESTONE.defaultBlockState())).build();

        AMETHYST_VOID = new ImmutableList.Builder<ProcessorRule>()
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.VOID_LANTERN_BLOCK.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.VOID_STONE.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.CRYING_OBSIDIAN.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.AMETHYST_BLOCK.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.05f), AlwaysTrueTest.INSTANCE, Blocks.PURPUR_PILLAR.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.05f), AlwaysTrueTest.INSTANCE, Blocks.PURPUR_BLOCK.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.ENDERRITE_BLOCK.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, ModBlocks.VOID_STONE.get().defaultBlockState())).build();

        GILDED_HOARD = new ImmutableList.Builder<ProcessorRule>()
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.RAW_GOLD_BLOCK.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.GOLD_BLOCK.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.YELLOW_CONCRETE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.GILDED_BLACKSTONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, Blocks.RAW_GOLD_BLOCK.defaultBlockState())).build();

        ANCIENT_ENDER = new ImmutableList.Builder<ProcessorRule>()
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.VOID_INFUSED_END_STONE_BRICKS.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.END_STONE_PILLAR.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.POLISHED_END_STONE.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, ModBlocks.CHISELED_END_STONE_BRICKS.get().defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.END_STONE.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.END_STONE_BRICKS.defaultBlockState()))
                .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, Blocks.END_STONE.defaultBlockState())).build();


        DungeonType.DARK_CREEPY.setRules(CREEPY_DEEPSLATE);
        DungeonType.DEEP_BLOOD.setRules(DEEP_BLOOD);
        DungeonType.OLD_RUINS.setRules(OLD_RUINS);
        DungeonType.AMETHYST_VOID.setRules(AMETHYST_VOID);
        DungeonType.GILDED_HOARD.setRules(GILDED_HOARD);
        DungeonType.ANCIENT_ENDER.setRules(ANCIENT_ENDER);
    }
}
