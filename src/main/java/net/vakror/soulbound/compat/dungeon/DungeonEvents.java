package net.vakror.soulbound.compat.dungeon;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.FillBucketEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachment;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistry;
import net.vakror.soulbound.compat.dungeon.dimension.Dimensions;

public class DungeonEvents {
    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlayerEnterDungeon(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer && event.getLevel().dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                ServerLevel world = (ServerLevel) event.getLevel();
                DungeonAttachment dungeonCapability = world.getData(DungeonAttachments.DUNGEON_ATTACHMENT);
                ResourceLocation type = DungeonRegistry.randomDungeonType();
                Dungeon dungeon = DungeonRegistry.dungeons.get(type).dungeon().copy();
                dungeon.setType(type);
                dungeonCapability.setDungeon(dungeon);
                world.getServer().sendSystemMessage(Component.literal(String.format(dungeonCapability.getDungeon().getJoinMessage(serverPlayer, (ServerLevel) event.getLevel()), serverPlayer.getDisplayName().getString())));
            }
        }

        @SubscribeEvent
        public static void onDungeonTick(TickEvent.LevelTickEvent event) {
            if (event.phase.equals(TickEvent.Phase.START) && !event.level.isClientSide && event.level.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE) && !event.level.players().isEmpty()) {
                int i = 1;
                DungeonAttachment dungeonCapability = event.level.getData(DungeonAttachments.DUNGEON_ATTACHMENT);
                ServerPlayer player = (ServerPlayer) event.level.players().get(0);
                if (!dungeonCapability.getDungeon().hasGenerated() && DungeonUtils.generateDungeon(event, dungeonCapability)) {
                    dungeonCapability.getDungeon().setHasGenerated(true);
                }
                if (!dungeonCapability.getDungeon().hasFirstTickElapsed) {
                    Vec3 spawn = dungeonCapability.getSetup().getPlayerSpawnPoint(event.level);
                    if (spawn != null) {
                        player.setPos(spawn);
                    }
                    DungeonUtils.setupDungeon(event, dungeonCapability);
                    dungeonCapability.getDungeon().setHasFirstTickElapsed(true);
                } else {
                    DungeonRegistry.dungeons.get(dungeonCapability.getDungeon().type).ticker().tick((ServerLevel) event.level);
                }
            }
        }

        @SubscribeEvent
        public static void forbidPlacingBlocksInDungeon(BlockEvent.EntityPlaceEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer)) {
                return;
            }
            if (event.getEntity().level().dimensionTypeId() != Dimensions.DUNGEON_TYPE) {
                return;
            }
            if (((ServerPlayer) event.getEntity()).isCreative()) {
                return;
            }
            DungeonAttachment dungeon = ((ServerLevel) event.getLevel()).getData(DungeonAttachments.DUNGEON_ATTACHMENT);
            if (dungeon.getSetup().shouldForbidPlacingOrMiningInDungeon((ServerLevel) event.getLevel())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void fillBucket(FillBucketEvent event) {
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

            DungeonAttachment dungeon = event.getLevel().getData(DungeonAttachments.DUNGEON_ATTACHMENT);
                if (dungeon.getSetup().shouldForbidPlacingOrMiningInDungeon(event.getLevel())) {
                    event.setCanceled(true);
                }
        }


        @SubscribeEvent
        public static void forbidBreakingBlocksInDungeon(BlockEvent.BreakEvent event) {
            if (event.getPlayer().isCreative()) {
                return;
            }
            DungeonAttachment dungeon = ((ServerLevel)event.getLevel()).getData(DungeonAttachments.DUNGEON_ATTACHMENT);
                if (dungeon.getSetup().shouldForbidPlacingOrMiningInDungeon((ServerLevel) event.getLevel())) {
                    event.setCanceled(true);
                }
        }

        @SubscribeEvent
        public static void explosionModify(ExplosionEvent.Detonate event) {
            // I only care about explosions in the Dungeon Dimension
            if (!event.getLevel().dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
                return;
            }
            if (event.getLevel().isClientSide) {
                return;
            }
            DungeonAttachment dungeon = event.getLevel().getData(DungeonAttachments.DUNGEON_ATTACHMENT);
                if (dungeon.getSetup().shouldForbidPlacingOrMiningInDungeon(event.getLevel())) {
                    event.getAffectedBlocks().clear();
                    event.getAffectedEntities().clear();
                }
            event.getExplosion().clearToBlow();
        }
    }
}