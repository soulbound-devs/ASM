package net.vakror.soulbound.compat.hammerspace.structure.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.vakror.soulbound.compat.hammerspace.blocks.ModDungeonBlocks;

import java.util.List;

public class DungeonProcessorRules {
    public static final List<ProcessorRule> CREEPY_DEEPSLATE = new ImmutableList.Builder<ProcessorRule>()
            .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE_BRICKS.defaultBlockState()))
            .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState()))
            .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE_TILES.defaultBlockState()))
            .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.2f), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState()))
            .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f), AlwaysTrueTest.INSTANCE, Blocks.DEEPSLATE.defaultBlockState()))
            .add(new ProcessorRule(new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 1f), AlwaysTrueTest.INSTANCE, Blocks.COBBLED_DEEPSLATE.defaultBlockState())).build();
}
