package net.vakror.soulbound.world.biome;

import net.minecraft.core.Registry;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.world.feature.ModPlacedFeatures;

public class ModBiomes {
    private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, SoulboundMod.MOD_ID);

    public static final Biome DEAD_FOREST_BIOME = new Biome.BiomeBuilder()
            .precipitation(Biome.Precipitation.NONE)
            .temperature(4)
            .mobSpawnSettings(new MobSpawnSettings.Builder()
                    .addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.HUSK, Weight.of(10), 1, 7)).build())
            .biomeCategory(Biome.BiomeCategory.NONE)
            .specialEffects(new BiomeSpecialEffects.Builder()
                    .fogColor(toColorInt(189, 179, 161, 255))
                    .grassColorOverride(toColorInt(161, 144, 116, 255))
                    .waterColor(toColorInt(163, 128, 67, 255))
                    .waterFogColor(toColorInt(240, 114, 105, 255))
                    .skyColor(toColorInt(184,154, 103, 255)).foliageColorOverride(toColorInt(107, 68, 0, 255)).build())
            .downfall(0)
            .generationSettings(BiomeGenerationSettings.EMPTY)
            .build();

    public static final RegistryObject<Biome> DEAD_FOREST = registerBiome("dead_forest", DEAD_FOREST_BIOME);

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
