package net.vakror.soulbound.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.SackItem;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.packets.SyncPickupModeC2SPacket;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModForgeEvents {
        @SubscribeEvent
        public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll(new ResourceLocation(SoulboundMod.MOD_ID, "soul"), SoulHudOverlay.HUD_SOUL);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.PICKUP_KEY);
        }
    }

    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.PICKUP_KEY.consumeClick()) {
                if (Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SackItem) {
                    ModPackets.sendToServer(new SyncPickupModeC2SPacket(Minecraft.getInstance().player.getUUID()));
                }
            }
        }
    }
}