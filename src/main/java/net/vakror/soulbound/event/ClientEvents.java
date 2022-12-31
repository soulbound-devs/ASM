package net.vakror.soulbound.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.client.SoulHudOverlay;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void registerGuiOverlay(FMLClientSetupEvent event) {
            OverlayRegistry.registerOverlayTop("soul", SoulHudOverlay.HUD_SOUL);
            OverlayRegistry.enableOverlay(SoulHudOverlay.HUD_SOUL, true);
        }
    }
}
