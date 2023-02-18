package net.vakror.soulbound.world.biome;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;

public class ModBiomes {
    private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, SoulboundMod.MOD_ID);


    public static Biome corrupted_cave() {
        MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();
        mobSpawnSettingsBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 10, 4, 6));
        mobSpawnSettingsBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.EVOKER, 25, 8, 8));
        BiomeDefaultFeatures.caveSpawns(mobSpawnSettingsBuilder);

        BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder = new BiomeGenerationSettings.Builder();
        OverworldBiomes.globalOverworldGeneration(biomeGenerationSettingsBuilder);
        biomeGenerationSettingsBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
        biomeGenerationSettingsBuilder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE_EXTRA_UNDERGROUND);
        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DEEP_DARK);
        return OverworldBiomes.biome(Biome.Precipitation.RAIN, 0.5F, 0.5F, mobSpawnSettingsBuilder, biomeGenerationSettingsBuilder, music);
    }

    public static final RegistryObject<Biome> CORRUPTED_CAVE = registerBiome("corrupted_cave", corrupted_cave());

    public static void register(IEventBus eventBus) {
        BIOMES.register(eventBus);
    }

    private static <I extends Biome> RegistryObject<I> registerBiome(String name, I biome) {
        return BIOMES.register(name, () -> biome);
    }

    public static int toColorInt(int R, int G, int B, int A) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}
