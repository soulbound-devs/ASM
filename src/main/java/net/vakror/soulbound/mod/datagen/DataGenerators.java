package net.vakror.soulbound.mod.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.datagen.worldgen.ore.OreData;


@Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        OreData.generate(event);
    }
}