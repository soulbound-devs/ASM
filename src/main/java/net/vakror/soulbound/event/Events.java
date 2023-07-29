package net.vakror.soulbound.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.custom.SoulCatalystBlock;
import net.vakror.soulbound.blocks.entity.custom.SoulCatalystBlockEntity;
import net.vakror.soulbound.capability.wand.ItemSeal;
import net.vakror.soulbound.capability.wand.ItemSealProvider;
import net.vakror.soulbound.entity.ModEntities;
import net.vakror.soulbound.entity.client.BroomModel;
import net.vakror.soulbound.entity.client.BroomRenderer;
import net.vakror.soulbound.items.custom.ActivatableSealableItem;
import net.vakror.soulbound.items.custom.SealableItem;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.packets.SyncSoulS2CPacket;
import net.vakror.soulbound.seal.tier.seal.IntegerTiered;
import net.vakror.soulbound.soul.PlayerSoul;
import net.vakror.soulbound.soul.PlayerSoulProvider;

import java.util.List;
import java.util.Optional;

public class Events {
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
//            if (event.getEntity() instanceof GoblaggerEntity && (isDamageType(event.getSource(), DamageTypes.CRAMMING) || isDamageType(event.getSource(), DamageTypes.IN_WALL) || isDamageType(event.getSource(), DamageTypes.FALL))) {
//                event.setCanceled(true);
//            }
            if (event.getSource().getEntity() instanceof ServerPlayer player) {
                if (player.getMainHandItem().getItem() instanceof SealableItem || player.getOffhandItem().getItem() instanceof SealableItem && event.getSource().getEntity() instanceof Player) {
                    InteractionHand hand;
                    if (player.getMainHandItem().getItem() instanceof SealableItem) {
                        hand = InteractionHand.MAIN_HAND;
                    } else {
                        hand = InteractionHand.OFF_HAND;
                    }
                    player.getItemInHand(hand).getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
                        List<Mob> nearbyMobs = getNearbyEntities(event.getEntity().level, player.blockPosition(), (float) 3, Mob.class);
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

        @SubscribeEvent
        public static void triggerSoulCatalyst(LivingDeathEvent event) {
            if (!event.getEntity().level.isClientSide) {
                if (event.getSource().getEntity() instanceof ServerPlayer player) {
                    Optional<BlockPos> catalyst = getNearestSoulCatalyst(event.getEntity().level, player.blockPosition(), 5);
                    if (catalyst.isPresent()) {
                        BlockPos pos = catalyst.get();
                        BlockEntity entity = event.getEntity().level.getBlockEntity(pos);
                        if (entity instanceof SoulCatalystBlockEntity catalystEntity) {
                            System.out.println(catalystEntity.getDelay());
                            if (catalystEntity.getDelay() == 0) {
                                catalystEntity.setDelay(catalystEntity.getMaxDelay());
                                player.getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent(playerSoul -> {
                                    if (event.getEntity() instanceof EnderDragon) {
                                        playerSoul.addDarkSoul(100);
                                        catalystEntity.setDelay(catalystEntity.getMaxDelay());
                                    } else if (event.getEntity() instanceof Monster monster) {
                                        playerSoul.addDarkSoul((int) (monster.getMaxHealth() * 0.3));
                                        catalystEntity.setDelay(catalystEntity.getMaxDelay());
                                    } else if (event.getEntity() instanceof Animal animal) {
                                        playerSoul.addSoul((int) (animal.getMaxHealth() * 0.2));
                                        catalystEntity.setDelay(catalystEntity.getMaxDelay());
                                    }
                                    ModPackets.sendToClient(new SyncSoulS2CPacket(playerSoul.getSoul(), playerSoul.getMaxSoul(), playerSoul.getDarkSoul(), playerSoul.getMaxDarkSoul()), (ServerPlayer) event.getSource().getEntity());
                                });
                            }
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void adjustBreakSpeed(PlayerEvent.BreakSpeed event) {
            float[] miningSpeed = new float[]{event.getOriginalSpeed()};
            ItemStack item = event.getEntity().getItemInHand(InteractionHand.MAIN_HAND);
            if (item.getItem() instanceof ActivatableSealableItem activatable) {
                if (item.isCorrectToolForDrops(event.getState())) {
                    activatable.getAllSealsWithProperty("haste").forEach((seal -> {
                        if (seal instanceof IntegerTiered tiered) {
                            miningSpeed[0] += (tiered.getAmount());
                            int a = tiered.getAmount();
                        }
                    }));
                    event.setNewSpeed(miningSpeed[0]);
                }
            }
        }

        public static <T extends Entity> List<T> getNearbyEntities(final LevelAccessor world, final Vec3i pos, final float radius, final Class<T> entityClass) {
            final AABB selectBox = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).move(pos.getX(), pos.getY(), pos.getZ()).inflate(radius);
            return world.getEntitiesOfClass(entityClass, selectBox, entity -> entity.isAlive() && !entity.isSpectator());
        }

        public static Optional<BlockPos> getNearestSoulCatalyst(final LevelAccessor world, BlockPos pos, final int radius) {
            return BlockPos.findClosestMatch(pos, radius, radius, (blockPos -> world.getBlockState(blockPos).getBlock() instanceof SoulCatalystBlock));
        }

        @SubscribeEvent
        public static void attachSealCapability(AttachCapabilitiesEvent<ItemStack> event) {
            if (event.getObject().getItem() instanceof SealableItem) {
                event.addCapability(new ResourceLocation(SoulboundMod.MOD_ID, "seals"), new ItemSealProvider());
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
            event.register(ItemSeal.class);
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

        @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ModEvents {

            @SubscribeEvent
            public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
                event.registerEntityRenderer(ModEntities.BROOM.get(), BroomRenderer::new);
            }

            @SubscribeEvent
            public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
                event.registerLayerDefinition(BroomModel.LAYER_LOCATION, BroomModel::createBodyLayer);
            }
        }
    }
}