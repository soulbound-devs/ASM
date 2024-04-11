package net.vakror.soulbound.world.biome;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.vakror.soulbound.SoulboundMod;

public class ModBiomes {
    public static final ResourceKey<Biome> CORRUPTED_CAVE = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(SoulboundMod.MOD_ID, "corrupted_cave"));
}
