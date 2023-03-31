package net.vakror.asm.world.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DungeonTeleporter implements ITeleporter {
    protected final ServerLevel level;
    protected final BlockPos pos;

    public DungeonTeleporter(ServerLevel level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        return new PortalInfo(new Vec3(pos.above().getX(), pos.above().getY(), pos.above().getZ()), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
