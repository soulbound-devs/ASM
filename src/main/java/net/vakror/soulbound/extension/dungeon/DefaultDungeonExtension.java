package net.vakror.soulbound.extension.dungeon;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.vakror.registry.jamesregistryapi.api.AbstractExtension;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.Dungeon;
import net.vakror.soulbound.compat.dungeon.DungeonTicker;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistrationContext;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistryEntry;
import net.vakror.soulbound.compat.dungeon.setup.StructureDungeonSetup;
import net.vakror.soulbound.compat.dungeon.theme.ThemeList;
import net.vakror.soulbound.extension.dungeon.structure.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DefaultDungeonExtension extends AbstractExtension<DungeonRegistrationContext> {
    @Override
    public ResourceLocation getExtensionName() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "default_dungeon");
    }

    @Override
    public void register() {
        context.addDungeon(new ResourceLocation("test", "test"), new DungeonRegistryEntry(new StructureDungeonSetup() {
            @Override
            public DungeonStructure getStructure(ServerLevel level) {
                return getStructureFromResourceLocation(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon"), level);
            }

            @Override
            public boolean canExit(Level level) {
                return false;
            }

            @Override
            public boolean shouldForbidPlacingOrMiningInDungeon(Level level) {
                return false;
            }

            @Override
            public @NotNull List<ThemeList> getThemes(ServerLevel level) {
                return List.of(new ThemeList().addTheme(DefaultDungeonThemes.TEST), new ThemeList().addTheme(DefaultDungeonThemes.TEST1));
            }
        }, new DungeonTicker() {
            @Override
            public void tick(ServerLevel level) {

            }
        }, new Dungeon()));
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
