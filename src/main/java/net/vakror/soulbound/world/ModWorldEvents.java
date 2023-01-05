package net.vakror.soulbound.world;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.world.feature.gen.ModTreeGen;

@Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID)
public class ModWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModTreeGen.generateTrees(event);
    }
}
