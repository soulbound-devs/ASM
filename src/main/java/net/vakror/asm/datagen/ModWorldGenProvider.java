package net.vakror.asm.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.vakror.asm.ASMMod;
import net.vakror.asm.world.biome.ModBiomes;
import net.vakror.asm.world.feature.ModConfiguredFeatures;
import net.vakror.asm.world.structure.ModStructures;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModConfiguredFeatures::placedBootstrap)
            .add(Registries.STRUCTURE_SET, ModStructures::bootstrapSets)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Collections.singleton(ASMMod.MOD_ID));
    }

}
