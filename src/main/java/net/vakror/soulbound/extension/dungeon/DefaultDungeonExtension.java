package net.vakror.soulbound.extension.dungeon;

import net.minecraft.resources.ResourceLocation;
import net.vakror.registry.jamesregistryapi.api.AbstractExtension;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistrationContext;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistryEntry;
import net.vakror.soulbound.extension.dungeon.builtin.DefaultDungeon;
import net.vakror.soulbound.extension.dungeon.builtin.DefaultDungeonSetup;
import net.vakror.soulbound.extension.dungeon.builtin.DefaultDungeonTicker;

import java.util.List;
import java.util.Optional;

public class DefaultDungeonExtension extends AbstractExtension<DungeonRegistrationContext> {
    @Override
    public ResourceLocation getExtensionName() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "default_dungeon");
    }

    @Override
    public void register() {
        context.addDungeon(new ResourceLocation(SoulboundMod.MOD_ID, "labyrinth_1"), new DungeonRegistryEntry(new DefaultDungeonSetup(), new DefaultDungeonTicker(), new DefaultDungeon()));
    }

    @Override
    public DungeonRegistrationContext getDefaultContext() {
        return new DungeonRegistrationContext();
    }

    //todo: remove this in the api
    @Override
    public Optional<List<ResourceLocation>> getAllowedContexts() {
        return Optional.empty();
    }
}
