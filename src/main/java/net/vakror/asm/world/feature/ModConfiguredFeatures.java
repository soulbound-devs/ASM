package net.vakror.asm.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    public static void placedBootstrap(BootstapContext<PlacedFeature> bootstrapContext) {
        bootstrapContext.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ASMMod.MOD_ID, "corrupted_tree_placed")), new PlacedFeature(getHolder(Registries.CONFIGURED_FEATURE, "corrupted_tree_spawn", bootstrapContext), VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 3))));
        bootstrapContext.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ASMMod.MOD_ID, "corrupted_tree_checked")), new PlacedFeature(getHolder(Registries.CONFIGURED_FEATURE, "corrupted_tree", bootstrapContext), List.of(PlacementUtils.filteredByBlockSurvival(Blocks.SCULK))));
    }

    public static <T, S> Holder<T> getHolder(ResourceKey<Registry<T>> registry, String name, BootstapContext<S> context) {
        return context.lookup(registry).get(ResourceKey.create(registry, new ResourceLocation(ASMMod.MOD_ID, name))).get();
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        bootstrapContext.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ASMMod.MOD_ID, "corrupted_tree")), new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(ModBlocks.CORRUPTED_LOG.get()), new StraightTrunkPlacer(1, 1, 1), BlockStateProvider.simple(ModBlocks.CORRUPTED_LEAVES.get()), new BlobFoliagePlacer(ConstantInt.of(1), ConstantInt.of(2), 1), new TwoLayersFeatureSize(1, 1, 2)).dirt(BlockStateProvider.simple(Blocks.SCULK)).build()));
        bootstrapContext.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ASMMod.MOD_ID, "corrupted_tree_spawn")), new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(
                        getHolder(Registries.PLACED_FEATURE, "corrupted_tree_checked", bootstrapContext),
                        0.005F)), getHolder(Registries.PLACED_FEATURE, "corrupted_tree_checked", bootstrapContext))));
    }
}