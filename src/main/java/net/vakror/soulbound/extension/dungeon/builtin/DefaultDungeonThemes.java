package net.vakror.soulbound.extension.dungeon.builtin;

import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.dungeon.theme.RuleDungeonTheme;

public class DefaultDungeonThemes {
    public static RuleDungeonTheme TEST = new RuleDungeonTheme.Builder()
            .addRule(new ProcessorRule(
                    new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f),
                    AlwaysTrueTest.INSTANCE,
                    ModBlocks.ANCIENT_OAK_PLANKS.get().defaultBlockState()
            )).build();

    public static RuleDungeonTheme TEST1 = new RuleDungeonTheme.Builder()
            .addRule(new ProcessorRule(
                    new RandomBlockMatchTest(ModDungeonBlocks.DUNGEON_BORDER.get(), 0.1f),
                    AlwaysTrueTest.INSTANCE,
                    ModBlocks.TUNGSTEN_ORE.get().defaultBlockState()
            )).build();
}
