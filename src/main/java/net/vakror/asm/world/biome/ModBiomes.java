package net.vakror.asm.world.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.vakror.asm.ASMMod;
import net.vakror.asm.mixin.OverworldBiomesInvoker;

public class ModBiomes {
    public static Biome corrupted_cave(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
        mobSpawnSettingsBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 10, 4, 6));
        mobSpawnSettingsBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.EVOKER, 25, 8, 8));
        BiomeDefaultFeatures.caveSpawns(mobSpawnSettingsBuilder);
        HolderGetter<PlacedFeature> holdergetter = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> holdergetter1 = context.lookup(Registries.CONFIGURED_CARVER);
        BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder = new BiomeGenerationSettings.Builder(holdergetter, holdergetter1);
        OverworldBiomes.globalOverworldGeneration(biomeGenerationSettingsBuilder);
        biomeGenerationSettingsBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        biomeGenerationSettingsBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DEEP_DARK);
        // Params bool precipitation, float temperature, float downfall, MobSpawnSettings, GenerationSettings, music
        return OverworldBiomesInvoker.invokeBiome(false, 0.5F, 0.5F, mobSpawnSettingsBuilder, biomeGenerationSettingsBuilder, music);
    }

    public static void bootstrap(BootstapContext<Biome> context) {
        Biome corrupted_cave = corrupted_cave(context);

        context.register(CORRUPTED_CAVE, corrupted_cave);
    }

    public static final ResourceKey<Biome> CORRUPTED_CAVE = ResourceKey.create(Registries.BIOME, new ResourceLocation(ASMMod.MOD_ID, "corrupted_cave"));
}
