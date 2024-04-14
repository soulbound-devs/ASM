package net.vakror.soulbound.compat.dungeon.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DungeonRegistry {
    public static final Map<ResourceLocation, DungeonRegistryEntry> dungeons = new HashMap<>();

    public static ResourceLocation randomDungeonType() {
        return dungeons.keySet().toArray(new ResourceLocation[0])[new Random().nextInt(dungeons.size())];
    }
}
