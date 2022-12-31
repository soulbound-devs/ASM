package net.vakror.soulbound.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.networking.ModPackets;
import net.vakror.soulbound.networking.SyncSoulS2CPacket;
import net.vakror.soulbound.soul.PlayerSoul;
import net.vakror.soulbound.soul.PlayerSoulProvider;
import net.vakror.soulbound.wand.Wand;
import net.vakror.soulbound.wand.ItemWandProvider;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                if (!event.getObject().getCapability(PlayerSoulProvider.PLAYER_SOUL).isPresent()) {
                    event.addCapability(new ResourceLocation(SoulboundMod.MOD_ID, "soul_provider"), new PlayerSoulProvider());
                }
            }
        }
        @SubscribeEvent
        public static void onAttachCapabilitiesItem(AttachCapabilitiesEvent<ItemStack> event) {
            if (event.getObject().getItem() instanceof WandItem) {
                if (!event.getObject().getCapability(ItemWandProvider.WAND).isPresent()) {
                    event.addCapability(new ResourceLocation(SoulboundMod.MOD_ID, "seals"), new ItemWandProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                event.getOriginal().getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent(oldStore -> {
                    event.getOriginal().getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerSoul.class);
            event.register(Wand.class);
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.side == LogicalSide.SERVER) {
                event.player.getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent(soul -> {
                    if (soul.getSoul() <= soul.getMaxSoul() && event.player.getRandom().nextFloat() < 0.005f) {
                        soul.addSoul(soul.getSoulRegenPerSecond() * 5);
                        soul.addDarkSoul(soul.getDarkSoulRegenPerSecond() * 5);
                        ModPackets.sendToClient(new SyncSoulS2CPacket(soul.getSoul(), soul.getMaxSoul(), soul.getDarkSoul(), soul.getMaxDarkSoul()), (ServerPlayer) event.player);
                    }
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
            if (!event.getWorld().isClientSide) {
                if (event.getEntity() instanceof ServerPlayer player) {
                    player.getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent(soul -> {
                        ModPackets.sendToClient(new SyncSoulS2CPacket(soul.getSoul(), soul.getMaxSoul(), soul.getDarkSoul(), soul.getMaxDarkSoul()), player);
                    });
                }
            }
        }
    }
}