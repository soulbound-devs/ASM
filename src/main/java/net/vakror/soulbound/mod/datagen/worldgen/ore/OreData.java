package net.vakror.soulbound.mod.datagen.worldgen.ore;

import net.minecraftforge.data.event.GatherDataEvent;

public class OreData {

    public static void generate(GatherDataEvent event) {
        ConfiguredOreData.generate(event);
        PlacedOreData.generate(event);
    }
}