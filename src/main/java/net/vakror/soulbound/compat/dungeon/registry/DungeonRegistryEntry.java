package net.vakror.soulbound.compat.dungeon.registry;

import net.vakror.soulbound.compat.dungeon.Dungeon;
import net.vakror.soulbound.compat.dungeon.setup.DungeonSetup;
import net.vakror.soulbound.compat.dungeon.DungeonTicker;

public record DungeonRegistryEntry(DungeonSetup placer, DungeonTicker ticker, Dungeon dungeon) {
    public DungeonRegistryEntry(DungeonSetup placer, DungeonTicker ticker, Dungeon dungeon) {
        this.placer = placer;
        this.ticker = ticker;
        this.dungeon = dungeon;

        this.placer.setDungeonRegistryEntry(this);
        this.ticker.setDungeonRegistryEntry(this);
    }
}
