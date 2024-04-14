package net.vakror.soulbound.extension.dungeon.builtin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.setup.StructureDungeonSetup;
import net.vakror.soulbound.compat.dungeon.theme.ThemeList;
import net.vakror.soulbound.extension.dungeon.structure.DungeonStructure;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultDungeonSetup extends StructureDungeonSetup {
    @Override
    public DungeonStructure getStructure(ServerLevel level) {
        return getStructureFromResourceLocation(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon"), level);
    }

    @Override
    public @NotNull Vec3 getPlayerSpawnPoint(Level level) {
        return new Vec3(0, 64, 0);
    }

    @Override
    public boolean canExit(Level level) {
        return true;
    }

    @Override
    public boolean shouldForbidPlacingOrMiningInDungeon(Level level) {
        return false;
    }

    @Override
    public @NotNull List<ThemeList> getThemes(ServerLevel level) {
        return List.of(new ThemeList().addTheme(DefaultDungeonThemes.TEST), new ThemeList().addTheme(DefaultDungeonThemes.TEST1));
    }
}
