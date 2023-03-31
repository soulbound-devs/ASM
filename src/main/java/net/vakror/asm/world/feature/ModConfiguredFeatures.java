package net.vakror.asm.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, ASMMod.MOD_ID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> CORRUPTED_TREE =
            CONFIGURED_FEATURES.register("corrupted_tree", () ->
                    new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                            BlockStateProvider.simple(ModBlocks.CORRUPTED_LOG.get()),
                            new StraightTrunkPlacer(1, 1, 1),
                            BlockStateProvider.simple(ModBlocks.CORRUPTED_LEAVES.get()),
                            new BlobFoliagePlacer(ConstantInt.of(1), ConstantInt.of(2), 1),
                            new TwoLayersFeatureSize(1, 1, 2)).dirt(BlockStateProvider.simple(Blocks.SCULK)).build()));


    public static final RegistryObject<PlacedFeature> CORRUPTED_TREE_CHECKED = ModPlacedFeatures.PLACED_FEATURES.register("corrupted_tree_checked",
            () -> new PlacedFeature(CORRUPTED_TREE.getHolder().get(),
                    List.of(PlacementUtils.filteredByBlockSurvival(Blocks.SCULK))));

    public static final RegistryObject<ConfiguredFeature<?, ?>> CORRUPTED_TREE_SPAWN =
            CONFIGURED_FEATURES.register("corrupted_tree_spawn", () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(
                            CORRUPTED_TREE_CHECKED.getHolder().get(),
                            0.005F)), CORRUPTED_TREE_CHECKED.getHolder().get())));


    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}