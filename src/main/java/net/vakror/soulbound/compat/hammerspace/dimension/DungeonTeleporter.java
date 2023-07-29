package net.vakror.soulbound.compat.hammerspace.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.vakror.soulbound.compat.hammerspace.blocks.custom.DungeonAccessBlock;
import net.vakror.soulbound.compat.hammerspace.blocks.custom.ReturnToOverworldBlock;
import net.vakror.soulbound.compat.hammerspace.dungeon.capability.DungeonProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DungeonTeleporter implements ITeleporter {
    protected BlockPos pos;
    protected BaseEntityBlock block;
    protected ServerLevel level;

    public DungeonTeleporter(BlockPos pos, BaseEntityBlock block) {
        this.pos = pos;
        this.block = block;
    }
    public DungeonTeleporter(BlockPos pos, BaseEntityBlock block, ServerLevel level) {
        this.pos = pos;
        this.block = block;
        this.level = level;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        if (block instanceof DungeonAccessBlock) {
            pos = new BlockPos(0, 64, 0);
        }
        if (block instanceof ReturnToOverworldBlock) {
            pos = new BlockPos(destWorld.getLevelData().getXSpawn(), destWorld.getLevelData().getYSpawn(), destWorld.getLevelData().getZSpawn());
            if (this.level != null) {
                this.level.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeonLevel -> {
                    if (!dungeonLevel.isStable()) {
                        dungeonLevel.setCanEnter(false);
                    }
                }));
            }
        }
        return new PortalInfo(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    public static boolean loaded(ServerLevel pLevel, ChunkPos pStart, ChunkPos pEnd) {
        return ChunkPos.rangeClosed(pStart, pEnd).allMatch((pos) -> pLevel.isLoaded(pos.getWorldPosition()));
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
