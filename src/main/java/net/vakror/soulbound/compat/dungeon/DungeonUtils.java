package net.vakror.soulbound.compat.dungeon;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.TickEvent;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachment;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistry;
import net.vakror.soulbound.compat.dungeon.setup.DungeonSetup;

public class DungeonUtils {
    static boolean generateDungeon(TickEvent.LevelTickEvent event, DungeonAttachment dungeonData) {
        DungeonSetup.State state = DungeonRegistry.dungeons.get(dungeonData.getDungeon().type).placer().place((ServerLevel) event.level, event.level.getServer().registryAccess());
        if (state.equals(DungeonSetup.State.FAIL)) {
            throw new IllegalStateException("Failed To Generate Dungeon", DungeonSetup.State.FAIL.getException());
        } else if (state.equals(DungeonSetup.State.RETRY)) {
            return false;
        }
        DungeonSetup.State.FAIL.setException(null);
        return true;
    }

    static void setupDungeon(TickEvent.LevelTickEvent event, DungeonAttachment dungeonData) {
        placeReturnBlockUnderneathEachPlayer(event);
    }

    private static void placeReturnBlockUnderneathEachPlayer(TickEvent.LevelTickEvent event) {
        for (Player player : event.level.players()) {
            event.level.setBlock(player.blockPosition().below(), ModDungeonBlocks.RETURN_TO_OVERWORLD_BLOCK.get().defaultBlockState(), 3);
        }
    }
}
