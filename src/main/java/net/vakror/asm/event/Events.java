package net.vakror.asm.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.entity.custom.DungeonAccessBlockEntity;
import net.vakror.asm.capability.wand.ItemSeal;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.entity.ModEntities;
import net.vakror.asm.entity.client.BroomModel;
import net.vakror.asm.entity.client.BroomRenderer;
import net.vakror.asm.items.custom.SealableItem;
import net.vakror.asm.items.custom.WandItem;
import net.vakror.asm.packets.ModPackets;
import net.vakror.asm.packets.SyncSoulS2CPacket;
import net.vakror.asm.soul.PlayerSoul;
import net.vakror.asm.soul.PlayerSoulProvider;
import net.vakror.asm.world.dimension.Dimensions;
import net.vakror.asm.world.structure.DungeonStructure;
import net.vakror.asm.world.structure.ModStructures;

import java.util.List;

public class Events {
    @Mod.EventBusSubscriber(modid = ASMMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                if (!event.getObject().getCapability(PlayerSoulProvider.PLAYER_SOUL).isPresent()) {
                    event.addCapability(new ResourceLocation(ASMMod.MOD_ID, "soul_provider"), new PlayerSoulProvider());
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
                    player.getItemInHand(hand).getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
                        List<Mob> nearbyMobs = getNearbyEntities(event.getEntity().level(), player.blockPosition(), (float) 3, Mob.class);
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
            final AABB selectBox = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).move(pos.getX(), pos.getY(), pos.getZ()).inflate(radius);
            return world.getEntitiesOfClass(entityClass, selectBox, entity -> entity.isAlive() && !entity.isSpectator());
        }

        @SubscribeEvent
        public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
            if (event.getObject().getItem() instanceof SealableItem) {
                event.addCapability(new ResourceLocation(ASMMod.MOD_ID, "seals"), new ItemSealProvider());
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

        @SubscribeEvent
        public static void onPlayerEnterDungeon(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide && event.getEntity() instanceof Player) {
                ServerLevel world = (ServerLevel) event.getLevel();
                if (world.dimensionType() == world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(Dimensions.DUNGEON_TYPE).get()) {
                    if ((event.getLevel().getBlockEntity(event.getEntity().blockPosition().below()) instanceof DungeonAccessBlockEntity entity)) {
                        genDungeon(entity, world, event);
                    } else {
                        world.setBlock(event.getEntity().blockPosition().below(), ModBlocks.DUNGEON_KEY_BLOCK.get().defaultBlockState(), 3);
                        if (event.getLevel().getBlockEntity(event.getEntity().blockPosition().below()) instanceof DungeonAccessBlockEntity entity) {
                            genDungeon(entity, world, event);
                        }
                    }
                }
            }
        }

        private static void genDungeon(DungeonAccessBlockEntity entity, ServerLevel world, EntityJoinLevelEvent event) {
            StructureStart start = new DungeonStructure(
                    ModStructures.structure(), entity.getDungeonSize(), 0)
                    .generate(world.registryAccess(),
                            world.getChunkSource().getGenerator(),
                            world.getChunkSource().getGenerator().getBiomeSource(),
                            world.getChunkSource().randomState(),
                            world.getStructureManager(),
                            world.getSeed(),
                            new ChunkPos(event.getEntity().blockPosition().below()),
                            0,
                            world,
                            (biomeHolder) -> true);
            BoundingBox boundingbox = start.getBoundingBox();
            ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
            ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
            ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((chunkPos) -> {
                start.placeInChunk(world, world.structureManager(), world.getChunkSource().getGenerator(), world.getRandom(), new BoundingBox(chunkPos.getMinBlockX(), world.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), world.getMaxBuildHeight(), chunkPos.getMaxBlockZ()), chunkPos);
            });
            entity.hasGeneratedDungeon(true);
        }
    }

    @Mod.EventBusSubscriber(modid = ASMMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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