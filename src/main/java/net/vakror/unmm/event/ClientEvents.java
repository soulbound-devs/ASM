package net.vakror.unmm.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.unmm.UnmmMod;
import net.vakror.unmm.client.SoulHudOverlay;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = UnmmMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void registerGuiOverlay(FMLClientSetupEvent event) {
            OverlayRegistry.registerOverlayTop("soul", SoulHudOverlay.HUD_SOUL);
            OverlayRegistry.enableOverlay(SoulHudOverlay.HUD_SOUL, true);
        }
    }
}
