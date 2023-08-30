package net.vakror.soulbound.mod.datagen.worldgen.ore;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.data.event.GatherDataEvent;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.blocks.ModBlocks;
import net.vakror.soulbound.mod.datagen.DataGeneratorHelper;

import java.util.List;
import java.util.function.Supplier;

public final class ConfiguredOreData {
        public static Supplier<List<OreConfiguration.TargetBlockState>> TUNGSTEN_CONFIG = Suppliers.memoize(() -> List.of(
                OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.TUNGSTEN_ORE.get().defaultBlockState())));

        public static ConfiguredFeature<?, ?> TUNGSTEN_ORE = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(TUNGSTEN_CONFIG.get(), 7));

        public static DataGeneratorHelper<ConfiguredFeature<?, ?>> helper = new DataGeneratorHelper<>(Registry.CONFIGURED_FEATURE_REGISTRY, null, SoulboundMod.MOD_ID)
                .add("tungsten_ore", TUNGSTEN_ORE);
        public static void generate(GatherDataEvent event) {
            helper.end(event);
        }
    }