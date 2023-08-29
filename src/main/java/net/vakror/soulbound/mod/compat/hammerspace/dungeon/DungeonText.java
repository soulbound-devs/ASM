package net.vakror.soulbound.mod.compat.hammerspace.dungeon;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.vakror.soulbound.mod.compat.hammerspace.dungeon.capability.DungeonProvider;
import net.vakror.soulbound.mod.seal.Tooltip;

public class DungeonText {
    public static Component JOIN_MESSAGE(ServerPlayer joinedPlayer, ServerLevel dungeon) {
        Tooltip.TooltipComponentBuilder tooltip = new Tooltip.TooltipComponentBuilder();
        tooltip.addPart(joinedPlayer.getDisplayName().getString(), Tooltip.TooltipComponentBuilder.ColorCode.LIGHT_BLUE);
        final boolean[] isStable = {true};
        dungeon.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeonLevel -> {
            isStable[0] = dungeonLevel.isStable();
        }));
        tooltip.addPart(" Has Joined " + (isStable[0] ? "a " : "an ") + (isStable[0] ? "Stable" : "Unstable") + " Dungeon");
        return tooltip.build().getTooltip();
    }

    public static Component CANNOT_EXIT_UNTIL_BEATEN = new Tooltip.TooltipComponentBuilder()
            .addPart("You cannot exit this Unstable Dungeon, As It Has Not Been Beaten Yet").build().getTooltip();
}
