package net.vakror.soulbound.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.client.renderer.SoulHudOverlay;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.items.custom.SackItem;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.packets.SyncPickupModeC2SPacket;
import net.vakror.soulbound.util.ColorUtil;

import java.awt.*;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModForgeEvents {
        @SubscribeEvent
        public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("soul", SoulHudOverlay.HUD_SOUL);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.PICKUP_KEY);
        }

        @SubscribeEvent
        public void registerItemColors(final RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> new Color(ColorUtil.toColorInt(0, 0, 0, 255), true).getRGB(), ModItems.WAND.get());
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