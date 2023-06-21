package net.vakror.asm.event.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

/**
 * This event is fired after a {@link Entity Boss} gets killed in a {@link ServerLevel Dungeon}, and a new layer is ready to be generated.
 * <p>
 * <strong>Note:</strong> This is called <strong>BEFORE</strong> the entity dies.
 * <p>
 * This event is {@linkplain Cancelable cancellable} and has {@linkplain HasResult a result}.
 * If the event is canceled, the next layer will not be generated
 * The result is the next layer
 * <p>
 * This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus}
 * on the logical server.
 **/
@Cancelable
@HasResult
public class GenerateNextDungeonLayerEvent extends Event {
    private final ServerPlayer player;
    private final ServerLevel level;
    private final int currentLayer;

    public GenerateNextDungeonLayerEvent(ServerPlayer player, ServerLevel level, int currentLayer) {
        this.player = player;
        this.level = level;
        this.currentLayer = currentLayer;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public int getCurrentLayer() {
        return currentLayer;
    }
}
