package net.vakror.soulbound.compat.hammerspace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vakror.betterspawner.event.SpawnBatchEvent;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.hammerspace.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.hammerspace.blocks.entity.ReturnToOverWorldBlockEntity;
import net.vakror.soulbound.compat.hammerspace.dimension.Dimensions;
import net.vakror.soulbound.compat.hammerspace.dungeon.DungeonText;
import net.vakror.soulbound.compat.hammerspace.dungeon.capability.Dungeon;
import net.vakror.soulbound.compat.hammerspace.dungeon.capability.DungeonProvider;
import net.vakror.soulbound.compat.hammerspace.dungeon.level.DungeonLevels;
import net.vakror.soulbound.compat.hammerspace.dungeon.level.room.multi.MultiRoomDungeonLevel;
import net.vakror.soulbound.compat.hammerspace.entity.DungeonMonster;
import net.vakror.soulbound.compat.hammerspace.structure.DungeonStructure;
import net.vakror.soulbound.compat.hammerspace.structure.ModStructures;
import net.vakror.soulbound.compat.hammerspace.structure.piece.DungeonPiece;
import net.vakror.soulbound.compat.hammerspace.structure.type.DungeonType;
import net.vakror.soulbound.event.custom.GenerateFirstDungeonLayerEvent;
import net.vakror.soulbound.event.custom.GenerateNextDungeonLayerEvent;

import java.util.Objects;
import java.util.UUID;

public class DungeonEvents {
    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlayerEnterDungeon(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
                ServerLevel world = (ServerLevel) event.getLevel();
                world.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                    if (!dungeon.canEnter()) {
                        event.setCanceled(true);
                    }
                    if (dungeon.isStable()) {
                        dungeon.setMaxLevels(1);
                    }
                    SoulboundMod.instance.server.getPlayerList().broadcastSystemMessage(DungeonText.JOIN_MESSAGE(serverPlayer, world), false);
                }));
            }
        }

        @SubscribeEvent
        public static void attachDungeonCapability(AttachCapabilitiesEvent<Level> event) {
            if (event.getObject().dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                event.addCapability(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_data"), new DungeonProvider());
            }
        }

        @SubscribeEvent
        public static void triggerBossFightIfAllRoomsAreBeaten(TickEvent.LevelTickEvent event) {
            if (!event.level.isClientSide && event.level.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                event.level.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeonLevel -> {
                    if (dungeonLevel.getLevelsBeaten() > dungeonLevel.getLevelsGenerated()) {
                        genNextLevel(dungeonLevel, (ServerLevel) event.level);
                    }
                }));
            }
        }

        public static void dungeonTickEvent(TickEvent.LevelTickEvent event) {
            if (event.phase.equals(TickEvent.Phase.START)) {
                if (!event.level.isClientSide && event.level.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                    ServerLevel world = (ServerLevel) event.level;
                    world.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                        if (!dungeon.canEnter()) {
                            event.setCanceled(true);
                        }
                        BlockPos returnPos = new BlockPos(0, 63, 0);
                        if ((event.level.getBlockEntity(returnPos) instanceof ReturnToOverWorldBlockEntity entity)) {
                            genDungeon(entity, world, event);
                        } else {
                            world.setBlock(returnPos, ModDungeonBlocks.RETURN_TO_OVERWORLD_BLOCK.get().defaultBlockState(), 3);
                            if (event.level.getBlockEntity(returnPos) instanceof ReturnToOverWorldBlockEntity entity) {
                                genDungeon(entity, world, event);
                            }
                        }
                    }));
                }
            }
        }

        public static UUID lastDungeonMobDeathUUID;

        @SubscribeEvent
        public static void onDungeonMobDeath(LivingDeathEvent event) {
            if (!event.getEntity().level.isClientSide && event.getEntity().level.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                if (event.getEntity() instanceof DungeonMonster && event.getEntity().getUUID() != lastDungeonMobDeathUUID) {
                    event.getEntity().level.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                        if (dungeon.getCurrentLevel() instanceof MultiRoomDungeonLevel level) {
                            if (level.currentRoom() == 0) {
                                level.setCurrentRoom(1);
                            }
                            level.removeOneMobFromRoom(level.currentRoom());
                            if (level.mobs(level.currentRoom()) <= 0) {
                                level.setCurrentRoom(level.currentRoom() + 1);
                            }
                            lastDungeonMobDeathUUID = event.getEntity().getUUID();
                        }
                    }));
                }
            }
        }

        @SubscribeEvent
        public static void onBatchSummon(SpawnBatchEvent event) {
            if (!event.level.isClientSide && event.level.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                event.level.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                    if (dungeon.getCurrentLevel() instanceof MultiRoomDungeonLevel multiRoomDungeonLevel) {
                        if (multiRoomDungeonLevel.currentRoom() == 0) {
                            multiRoomDungeonLevel.setCurrentRoom(1);
                        }
                        multiRoomDungeonLevel.setMobAmount(multiRoomDungeonLevel.currentRoom() - 1, event.count);
                    }
                    System.out.println("count: " + event.count);
                    for (Entity entity : event.entities) {
                        System.out.println("entity: " + entity.getType());
                    }
                }));
            }
        }

        @SubscribeEvent
        public static void forbidPlacingBlocksInDungeon(BlockEvent.EntityPlaceEvent event) {
            //TODO: only forbid placing blocks if this is a stable dungeons where the boss has been beaten
            if (!(event.getEntity() instanceof ServerPlayer)) {
                return;
            }
            if (event.getEntity().level.dimensionTypeId() != Dimensions.DUNGEON_TYPE) {
                return;
            }
            if (!event.isCancelable()) {
                return;
            } if (((ServerPlayer) event.getEntity()).isCreative()) {
                return;
            }
            ((ServerLevel) event.getLevel()).getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                if (dungeon.isStable() && dungeon.getLevelsBeaten() >= 1) {
                    return;
                }
                event.setCanceled(true);
            }));
        }

        @SubscribeEvent
        public void fillBucket(FillBucketEvent event) {
            if (event.getLevel().isClientSide) {
                return;
            }
            if (event.getEntity().isCreative()) {
                return;
            }

            // I only care about taking liquids in the Dungeon Dimension
            if (!event.getLevel().dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                return;
            }

            event.getLevel().getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                if (dungeon.isStable() && dungeon.getLevelsBeaten() >= 1) {
                    return;
                }
                event.setCanceled(true);
            }));
            event.setCanceled(true);
        }

        @SubscribeEvent
        public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel().isClientSide) {
                return;
            }
            if (event.getEntity().isCreative()) {
                return;
            }

            // I only care about taking liquids in the Dungeon Dimension
            if (!event.getLevel().dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                return;
            }

            event.getLevel().getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                if (dungeon.isStable() && dungeon.getLevelsBeaten() >= 1) {
                    return;
                }
                event.setCanceled(true);
            }));
            event.setCanceled(true);
        }


        @SubscribeEvent
        public static void forbidBreakingBlocksInDungeon(BlockEvent.BreakEvent event) {
            if (event.getPlayer().isCreative()) {
                return;
            }
            ((ServerLevel) event.getLevel()).getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                if (dungeon.isStable() && dungeon.getLevelsBeaten() >= 1) {
                    return;
                }
                event.setCanceled(true);
            }));
            if (Objects.requireNonNull(event.getPlayer()).level.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                event.setCanceled(true);
            }
        }

        private static void genDungeon(ReturnToOverWorldBlockEntity entity, ServerLevel world, TickEvent.LevelTickEvent joinLevelEvent) {
            if (!joinLevelEvent.level.players().isEmpty() && !entity.hasGeneratedDungeon()) {
                DungeonType type = DungeonType.getRandomType();
                GenerateFirstDungeonLayerEvent dungeonLayerEvent = new GenerateFirstDungeonLayerEvent((ServerPlayer) joinLevelEvent.level.players().get(0), world, 0);
                MinecraftForge.EVENT_BUS.post(dungeonLayerEvent);
                DungeonPiece result = dungeonLayerEvent.getNewLayer();
                StructureStart start = new DungeonStructure(
                        ModStructures.structure(), entity.getDungeonSize(), 0, result, type, world)
                        .generate(world.registryAccess(),
                                world.getChunkSource().getGenerator(),
                                world.getChunkSource().getGenerator().getBiomeSource(),
                                world.getChunkSource().randomState(),
                                world.getStructureManager(),
                                world.getSeed(),
                                new ChunkPos(joinLevelEvent.level.players().get(0).blockPosition().below()),
                                0,
                                world,
                                (biomeHolder) -> true);
                BoundingBox boundingbox = start.getBoundingBox();
                ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
                ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
                ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((chunkPos) -> {
                    start.placeInChunk(world, world.structureManager(), world.getChunkSource().getGenerator(), world.getRandom(), new BoundingBox(chunkPos.getMinBlockX(), world.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), world.getMaxBuildHeight(), chunkPos.getMaxBlockZ()), chunkPos);
                });
                world.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeon -> {
                    //TODO: allow random current levels
                    dungeon.setCurrentLevel(DungeonLevels.LABYRINTH_50);
                    dungeon.setType(type);
                }));
                BlockPos returnPos = new BlockPos(0, 63, 0);
                world.setBlock(returnPos, ModDungeonBlocks.RETURN_TO_OVERWORLD_BLOCK.get().defaultBlockState(), 3);
                ((ReturnToOverWorldBlockEntity) Objects.requireNonNull(world.getBlockEntity(returnPos))).hasGeneratedDungeon(true);
            }
        }
    }

    @SubscribeEvent
    public void explosionModify(ExplosionEvent.Detonate event)
    {
        // I only care about explosions in the Dungeon Dimension
        if (!event.getLevel().dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
            return;
        }
        if (event.getLevel().isClientSide) {
            return;
        }
        if (event.getLevel().getCapability(DungeonProvider.DUNGEON).orElse(new Dungeon()).isStable() && event.getLevel().getCapability(DungeonProvider.DUNGEON).orElse(new Dungeon()).getLevelsBeaten() >=1) {
            return;
        }

        event.getExplosion().clearToBlow();
    }

    private static void genNextLevel(Dungeon dungeon, ServerLevel dungeonLevel) {
        if (dungeon.getLevelsBeaten() + 1 <= dungeon.getMaxLevels()) {
            GenerateNextDungeonLayerEvent dungeonLayerEvent = new GenerateNextDungeonLayerEvent(dungeonLevel.players().get(0), dungeonLevel, dungeon.getLevelsGenerated());
            boolean canceled = MinecraftForge.EVENT_BUS.post(dungeonLayerEvent);
            if (!canceled) {
                StructureStart start = new DungeonStructure(
                        ModStructures.structure(), dungeon.getCurrentLevel().size(), dungeon.getLevelsGenerated() + 1, null, dungeon.getType(), dungeonLevel)
                        .generate(dungeonLevel.registryAccess(),
                                dungeonLevel.getChunkSource().getGenerator(),
                                dungeonLevel.getChunkSource().getGenerator().getBiomeSource(),
                                dungeonLevel.getChunkSource().randomState(),
                                dungeonLevel.getStructureManager(),
                                dungeonLevel.getSeed(),
                                new ChunkPos(dungeonLevel.players().get(0).blockPosition().below()),
                                0,
                                dungeonLevel,
                                (biomeHolder) -> true);
                BoundingBox boundingbox = start.getBoundingBox();
                ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
                ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
                ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((chunkPos) -> {
                    start.placeInChunk(dungeonLevel, dungeonLevel.structureManager(), dungeonLevel.getChunkSource().getGenerator(), dungeonLevel.getRandom(), new BoundingBox(chunkPos.getMinBlockX(), dungeonLevel.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), dungeonLevel.getMaxBuildHeight(), chunkPos.getMaxBlockZ()), chunkPos);
                });
                dungeon.beatCurrentLevel();
                dungeon.setCurrentLevel(
                        switch (dungeon.getCurrentLevel().size()) {
                            default -> switch (dungeon.getLevelsGenerated()) {
                                default -> DungeonLevels.LABYRINTH_50;
                            };
                        });
            }
        }
    }
}