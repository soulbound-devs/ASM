package net.vakror.asm.dungeon;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.vakror.asm.dungeon.capability.DungeonProvider;
import net.vakror.asm.seal.Tooltip;

public class DungeonText {
    public static Component JOIN_MESSAGE(ServerPlayer joinedPlayer, ServerLevel dungeon) {
        Tooltip.TooltipComponentBuilder tooltip = new Tooltip.TooltipComponentBuilder();
        tooltip.addPart(joinedPlayer.getDisplayName().getString(), Tooltip.TooltipComponentBuilder.ColorCode.LIGHT_BLUE);
        final boolean[] isStable = {true};
        dungeon.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeonLevel -> {
            isStable[0] = dungeonLevel.isStable();
        }));
        tooltip.addPart(new StringBuilder().append(" Has Joined ").append(isStable[0] ? "a " : "an ").append(isStable[0] ? "Stable" : "Unstable").append(" Dungeon").toString());
        return tooltip.build().getTooltip();
    }

    public static Component CANNOT_EXIT_UNTIL_BEATEN = new Tooltip.TooltipComponentBuilder()
            .addPart("You cannot exit this Unstable Dungeon, As It Has Not Been Beaten Yet").build().getTooltip();
}
