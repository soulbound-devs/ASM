package net.vakror.soulbound.compat.dungeon.registry;

import net.minecraft.resources.ResourceLocation;
import net.vakror.registry.jamesregistryapi.api.context.IRegistrationContext;
import net.vakror.soulbound.SoulboundMod;

import java.util.Map;

public class DungeonRegistrationContext implements IRegistrationContext {
    public void addDungeon(ResourceLocation location, DungeonRegistryEntry entry) {
        addDungeon(Map.of(location, entry));
    }

    public void addDungeon(Map<ResourceLocation, DungeonRegistryEntry> dungeons) {
        DungeonRegistry.dungeons.putAll(dungeons);
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "default_dungeon_context");
    }
}
