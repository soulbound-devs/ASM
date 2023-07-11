package net.vakror.soulbound.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    public static void placedBootstrap(BootstapContext<PlacedFeature> bootstrapContext) {
        bootstrapContext.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(SoulboundMod.MOD_ID, "corrupted_tree_placed")), new PlacedFeature(getHolder(Registries.CONFIGURED_FEATURE, "corrupted_tree_spawn", bootstrapContext), List.of(RarityFilter.onAverageOnceEvery(5), HeightRangePlacement.uniform(VerticalAnchor.BOTTOM, VerticalAnchor.aboveBottom(64)))));
        bootstrapContext.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(SoulboundMod.MOD_ID, "corrupted_tree_checked")), new PlacedFeature(getHolder(Registries.CONFIGURED_FEATURE, "corrupted_tree", bootstrapContext), List.of(PlacementUtils.filteredByBlockSurvival(Blocks.SCULK))));
    }

    public static <T, S> Holder<T> getHolder(ResourceKey<Registry<T>> registry, String name, BootstapContext<S> context) {
        return context.lookup(registry).get(ResourceKey.create(registry, new ResourceLocation(SoulboundMod.MOD_ID, name))).get();
    }
    public static ConfiguredFeature<?, ?> CORRUPTED_TREE = new ConfiguredFeature<>(
            Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.CORRUPTED_LOG.get()),
                    new StraightTrunkPlacer(5, 3, 5),
                    BlockStateProvider.simple(ModBlocks.CORRUPTED_LEAVES.get()),
                    new BlobFoliagePlacer(ConstantInt.of(3),
                            ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(4, 2, 4))
                    .dirt(BlockStateProvider.simple(Blocks.SCULK)).build());
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        bootstrapContext.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(SoulboundMod.MOD_ID, "corrupted_tree")), CORRUPTED_TREE);
        bootstrapContext.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(SoulboundMod.MOD_ID, "corrupted_tree_spawn")), new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
                new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(
                        getHolder(Registries.PLACED_FEATURE, "corrupted_tree_checked", bootstrapContext),
                        0.05F)), getHolder(Registries.PLACED_FEATURE, "corrupted_tree_checked", bootstrapContext))));
    }
}