package net.vakror.soulbound.compat.dungeon.setup;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistryEntry;

public abstract class DungeonSetup {
    DungeonRegistryEntry dungeonRegistryEntry;
    public abstract State place(ServerLevel level, RegistryAccess access);

    /**
     * a good method to do this is to place a special unique marker block underneath the possible spawn positions, search for those, and then spawn the player there.
     * Then, in the ticker replace all of those with another kind of block.
     * However, the one the player spawns on will always be replaced with a return block.
     * @return the player's spawn position
     */
    public abstract Vec3 getPlayerSpawnPoint(Level level);

    public DungeonRegistryEntry getDungeonRegistryEntry() {
        return dungeonRegistryEntry;
    }

    public void setDungeonRegistryEntry(DungeonRegistryEntry dungeonRegistryEntry) {
        this.dungeonRegistryEntry = dungeonRegistryEntry;
    }

    public abstract boolean canExit(Level level);

    public abstract boolean shouldForbidPlacingOrMiningInDungeon(Level level);

    public enum State {
        FAIL,
        SUCCESS,
        RETRY;

        Exception e;
        State() {}
        State(Exception e) {

        }

        public Exception getException() {
            return e;
        }

        public void setException(Exception e) {
            this.e = e;
        }
    }
}
