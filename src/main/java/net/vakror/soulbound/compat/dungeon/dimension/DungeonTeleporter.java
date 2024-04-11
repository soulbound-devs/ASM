package net.vakror.soulbound.compat.dungeon.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.ITeleporter;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DungeonTeleporter implements ITeleporter {
    protected BlockPos pos;
    protected final BaseEntityBlock block;
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
        if (destWorld.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
            pos = new BlockPos(1, 64, 1);
        } else {
            pos = new BlockPos(destWorld.getLevelData().getXSpawn(), destWorld.getLevelData().getYSpawn(), destWorld.getLevelData().getZSpawn());
            if (this.level != null) {
                this.level.getExistingData(DungeonAttachments.DUNGEON_ATTACHMENT).ifPresent((dungeonLevel -> {
                    if (!dungeonLevel.getDungeon().canEnterAgainAfterExiting()) {
                        dungeonLevel.getDungeon().setEnterable(false);
                    }
                }));
            }
        }
        return new PortalInfo(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
