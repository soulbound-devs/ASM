package net.vakror.soulbound.compat.dungeon.dimension;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.ITeleporter;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class DungeonToOverworldTeleporter implements ITeleporter {

    public final ServerLevel currentLevel;

    public DungeonToOverworldTeleporter(ServerLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(@NotNull Entity entity, ServerLevel destWorld, @NotNull Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        if (destWorld.dimensionTypeId().equals(BuiltinDimensionTypes.OVERWORLD)) {
            AtomicReference<Vec3> pos = new AtomicReference<>(new Vec3(destWorld.getLevelData().getXSpawn(), destWorld.getLevelData().getYSpawn(), destWorld.getLevelData().getZSpawn()));
            if (this.currentLevel != null) {
                this.currentLevel.getExistingData(DungeonAttachments.DUNGEON_ATTACHMENT).ifPresent((dungeonLevel -> {
                    if (!dungeonLevel.getDungeon().canEnterAgainAfterExiting()) {
                        dungeonLevel.getDungeon().setEnterable(false);
                    }
                    pos.set(dungeonLevel.getReturnPos());
                }));
            }
            return new PortalInfo(pos.get(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
        }
        return null;
    }
}
