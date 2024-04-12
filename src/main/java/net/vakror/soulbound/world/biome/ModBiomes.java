package net.vakror.soulbound.world.biome;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.vakror.soulbound.SoulboundMod;

public class ModBiomes {
    public static final ResourceKey<Biome> CORRUPTED_CAVE = ResourceKey.create(Registries.BIOME, new ResourceLocation(SoulboundMod.MOD_ID, "corrupted_cave"));
}
