package net.vakror.soulbound.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.hammerspace.structure.ModStructures;
import net.vakror.soulbound.world.biome.ModBiomes;
import net.vakror.soulbound.world.carver.ModCarvers;
import net.vakror.soulbound.world.feature.ModConfiguredFeatures;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModConfiguredFeatures::placedBootstrap)
            .add(Registries.CONFIGURED_CARVER, ModCarvers::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructures::bootstrapSets)
            .add(Registries.STRUCTURE, ModStructures::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Collections.singleton(SoulboundMod.MOD_ID));
    }

}
