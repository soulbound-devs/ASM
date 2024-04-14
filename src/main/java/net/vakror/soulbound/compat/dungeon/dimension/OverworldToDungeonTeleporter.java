package net.vakror.soulbound.compat.dungeon.dimension;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.ITeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class OverworldToDungeonTeleporter implements ITeleporter {

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, @NotNull Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        Vec3 pos = entity.blockPosition().getCenter();
        if (destWorld.dimensionTypeId().equals(Dimensions.DUNGEON_TYPE)) {
            pos = new Vec3(0, 64, 0);
        }
        return new PortalInfo(pos, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
    }
}
