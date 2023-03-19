package net.vakror.soulbound.world.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraftforge.common.util.ITeleporter;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.blocks.custom.DungeonAccessBlock;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DungeonTeleporter implements ITeleporter {
    protected final ServerLevel level;

    public DungeonTeleporter(ServerLevel level) {
        this.level = level;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        destWorld.setBlock(new BlockPos(entity.position()).below(), ModBlocks.DUNGEON_KEY_BLOCK.get().defaultBlockState().setValue(DungeonAccessBlock.LOCKED, Boolean.valueOf(false)), 3);
        return new PortalInfo(entity.position(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
