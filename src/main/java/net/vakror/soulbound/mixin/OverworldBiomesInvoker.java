package net.vakror.soulbound.mixin;

import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(OverworldBiomes.class)
public interface OverworldBiomesInvoker {
    @Invoker("biome")
    static Biome invokeBiome(boolean precipitation, float temperature, float downfall, MobSpawnSettings.Builder mobSpawnSettingsBuilder, BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder, @Nullable Music music) {
        throw new AssertionError();
    }
}
