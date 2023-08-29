package net.vakror.soulbound.mod.event.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;
import net.vakror.soulbound.mod.compat.hammerspace.structure.piece.DungeonPiece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event is fired after a {@link ServerPlayer Player} enters a {@link ServerLevel Dungeon} for the first time, and a new layer is about to be generated.
 * <p>
 * <strong>Note:</strong> This is called <strong>BEFORE</strong> the layer generates.
 * <p>
 * This event is not {@linkplain Cancelable cancellable}, but has {@linkplain HasResult a result}.
 * The result is the layer to be generated
 * <p>
 * This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus}
 * on the logical server.
 **/
@HasResult
public class GenerateFirstDungeonLayerEvent extends Event {
    private final ServerPlayer player;
    private final ServerLevel level;
    @Nullable
    private DungeonPiece newLayer;
    private final int layer;

    public GenerateFirstDungeonLayerEvent(ServerPlayer player, ServerLevel level, int layer) {
        this.player = player;
        this.level = level;
        this.layer = layer;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public int getLayer() {
        return layer;
    }

    public void setNewLayer(@NotNull DungeonPiece newLayer) {
        this.newLayer = newLayer;
    }

    @Nullable
    public DungeonPiece getNewLayer() {
        return newLayer;
    }
}
