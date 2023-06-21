package net.vakror.asm.event.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event is fired whenever a {@link Entity Boss} gets killed in a {@link ServerLevel Dungeon}.
 * <p>
 * <strong>Note:</strong> This is called <strong>BEFORE</strong> the entity dies.
 * <p>
 * This event is {@linkplain Cancelable cancellable} and does not {@linkplain HasResult have a result}.
 * If the event is canceled, the boss will not die, instead summoning {@link #getNextStage() stage}
 * <p>
 * This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus}
 * on the logical server.
 **/
@Cancelable
public class DungeonBossKilledEvent extends Event {
    private final Level dungeon;
    private final Entity boss;
    private final ServerPlayer cause;
    private @Nullable Entity nextStage;

    public DungeonBossKilledEvent(Level dungeon, Entity boss, ServerPlayer player) {
        this.dungeon = dungeon;
        this.boss = boss;
        this.cause = player;
    }

    public Level getDungeon() {
        return dungeon;
    }

    public Entity getBoss() {
        return boss;
    }

    public ServerPlayer getCause() {
        return cause;
    }

    public void setNextStage(Entity nextStage) {
        this.nextStage = nextStage;
    }

    public void nextStage(@NotNull Entity boss) {
        this.nextStage = boss;
    }

    public @Nullable Entity getNextStage() {
        return this.nextStage;
    }
 }
