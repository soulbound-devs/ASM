package net.vakror.soulbound.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
import net.vakror.soulbound.wand.ItemWand;
import net.vakror.soulbound.wand.ItemWandProvider;
import net.vakror.soulbound.world.feature.ModPlacedFeatures;

import java.util.List;

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
        public static void triggerScytheAOE(LivingHurtEvent event) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (player.getMainHandItem().getItem() instanceof WandItem || player.getOffhandItem().getItem() instanceof WandItem) {
                    InteractionHand hand;
                    if (player.getMainHandItem().getItem() instanceof WandItem) {
                        hand = InteractionHand.MAIN_HAND;
                    } else {
                        hand = InteractionHand.OFF_HAND;
                    }
                    player.getItemInHand(hand).getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
                        List<Mob> nearbyMobs = getNearbyEntities(event.getEntity().level, player.blockPosition(), (float) 3, Mob.class);
                        nearbyMobs.remove((Mob) event.getEntity());
                        int i = 1;
                        for (Mob mob : nearbyMobs) {
                            mob.hurt(event.getSource(), event.getAmount());
                            i++;
                            if (i >= 50) {
                                break;
                            }
                        }
                    });
                }
            }
        }

        public static <T extends Entity> List<T> getNearbyEntities(final LevelAccessor world, final Vec3i pos, final float radius, final Class<T> entityClass) {
            final AABB selectBox = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).move((double) pos.getX(), (double) pos.getY(), (double) pos.getZ()).inflate((double) radius);
            return (List<T>) world.getEntitiesOfClass(entityClass, selectBox, entity -> entity.isAlive() && !entity.isSpectator());
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesItem(AttachCapabilitiesEvent<ItemStack> event) {
            if (event.getObject().getItem() instanceof WandItem) {
                event.addCapability(new ResourceLocation(SoulboundMod.MOD_ID, "seals"), new ItemWandProvider());
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
            event.register(ItemWand.class);
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
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide) {
                if (event.getEntity() instanceof ServerPlayer player) {
                    player.getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent(soul -> {
                        ModPackets.sendToClient(new SyncSoulS2CPacket(soul.getSoul(), soul.getMaxSoul(), soul.getDarkSoul(), soul.getMaxDarkSoul()), player);
                    });
                }
            }
        }
    }
}