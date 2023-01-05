package net.vakror.soulbound.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {


    public static final Holder<ConfiguredFeature<TreeConfiguration, ?>> ANCIENT_OAK_TREE =
            FeatureUtils.register("ancient_oak", Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.ANCIENT_OAK_LOG.get()),
                    new DarkOakTrunkPlacer(6, 3, 4),
                    BlockStateProvider.simple(Blocks.AIR),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4),
                    new TwoLayersFeatureSize(1, 0, 2)).build());

    public static final Holder<PlacedFeature> ANCIENT_OAK_CHECKED =
            PlacementUtils.register("ancient_oak_checked", ANCIENT_OAK_TREE, PlacementUtils.filteredByBlockSurvival(Blocks.GRASS_BLOCK));

    public static final Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> ANCIENT_OAK_SPAWN =
            FeatureUtils.register("ancient_oak_spawn", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(ANCIENT_OAK_CHECKED, 0.5F)), ANCIENT_OAK_CHECKED));

}