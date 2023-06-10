package net.vakror.asm.seal.function.use.custom;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vakror.asm.items.custom.SackItem;
import net.vakror.asm.seal.function.use.UseFunction;

import java.nio.file.Path;

public class OpenWandBackpackInventoryUseFunction extends UseFunction {
    public OpenWandBackpackInventoryUseFunction(JsonObject function, Path path) {
        super(function, path);
    }

    @Override
    public void readFromJson(JsonObject function, Path path) {
        super.readFromJson(function, path);
    }

    @Override
    public void executeUse(Level level, Player player, InteractionHand hand) {
        SackItem.openScreen((ServerPlayer) player, hand);
        super.executeUse(level, player, hand);
    }
}
