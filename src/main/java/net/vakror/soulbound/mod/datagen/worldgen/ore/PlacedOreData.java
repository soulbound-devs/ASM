package net.vakror.soulbound.mod.datagen.worldgen.ore;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.data.event.GatherDataEvent;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.datagen.DataGeneratorHelper;

import java.util.List;

public class PlacedOreData {
    public static PlacedFeature TUNGSTEN_ORE = new PlacedFeature(new Holder.Direct<>(ConfiguredOreData.TUNGSTEN_ORE),
            commonOrePlacement(7, // VeinsPerChunk
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
    public static DataGeneratorHelper<PlacedFeature> helper = new DataGeneratorHelper<>(Registry.PLACED_FEATURE_REGISTRY, null, SoulboundMod.MOD_ID)
            .add("tungsten_ore", TUNGSTEN_ORE);

    public static void generate(GatherDataEvent event) {
        helper.end(event);
    }

    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    public static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }
}
