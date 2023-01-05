package net.vakror.soulbound.world.biome;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.registries.ForgeRegistries;
import net.vakror.soulbound.SoulboundMod;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.SurfaceRuleManager;

import java.util.function.Consumer;

public class SoulboundRegion extends Region {
    public SoulboundRegion(ResourceLocation name, RegionType type, int weight) {
        super(name, type, weight);
    }

    @Override
    @SuppressWarnings("all")
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        mapper.accept(new Pair<Climate.ParameterPoint, ResourceKey<Biome>>(new Climate.ParameterPoint(Climate.Parameter.span(3, 5), Climate.Parameter.point(0), Climate.Parameter.span(0.3f, 0.7f), Climate.Parameter.span(0.4f, 0.6f), Climate.Parameter.span(0.2f, 0.6f), Climate.Parameter.span(0.7f, 2.6f), 4), ForgeRegistries.BIOMES.getResourceKey(ModBiomes.DEAD_FOREST_BIOME).get()));
        super.addBiomes(registry, mapper);
    }
}
