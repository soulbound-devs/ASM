package net.vakror.asm.world.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.blocks.custom.DungeonAccessBlock;
import net.vakror.asm.blocks.entity.custom.DungeonAccessBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DungeonTeleporter implements ITeleporter {
    protected final ServerLevel level;
    protected final BlockPos pos;
    protected final DungeonAccessBlock block;

    public DungeonTeleporter(ServerLevel level, BlockPos pos, DungeonAccessBlock block) {
        this.level = level;
        this.pos = pos;
        this.block = block;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        if (!(destWorld.getBlockEntity(pos) instanceof DungeonAccessBlockEntity)) {
            destWorld.setBlock(pos, ModBlocks.DUNGEON_KEY_BLOCK.get().defaultBlockState(), 3);
        }
        return new PortalInfo(new Vec3(pos.above().getX(), pos.above().getY(), pos.above().getZ()), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    public static boolean loaded(ServerLevel pLevel, ChunkPos pStart, ChunkPos pEnd) {
        return ChunkPos.rangeClosed(pStart, pEnd).allMatch((pos) -> pLevel.isLoaded(pos.getWorldPosition()));
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
