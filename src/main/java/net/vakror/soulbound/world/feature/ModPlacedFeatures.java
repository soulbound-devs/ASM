package net.vakror.soulbound.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.placement.*;

public class ModPlacedFeatures {
    public static final Holder<PlacedFeature> ANCIENT_OAK_PLACED = PlacementUtils.register("ancient_oak_placed",
            ModConfiguredFeatures.ANCIENT_OAK_SPAWN, VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(3)));

}
