package net.vakror.soulbound.compat.dungeon;

import net.minecraft.server.level.ServerLevel;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistryEntry;

public abstract class DungeonTicker {

    DungeonRegistryEntry dungeonRegistryEntry;

    public abstract void tick(ServerLevel level);

    public DungeonRegistryEntry getDungeonRegistryEntry() {
        return dungeonRegistryEntry;
    }

    public void setDungeonRegistryEntry(DungeonRegistryEntry dungeonRegistryEntry) {
        this.dungeonRegistryEntry = dungeonRegistryEntry;
    }
}
