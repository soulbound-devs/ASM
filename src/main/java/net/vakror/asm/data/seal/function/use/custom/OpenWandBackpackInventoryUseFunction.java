package net.vakror.asm.data.seal.function.use.custom;

import com.google.gson.JsonObject;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vakror.asm.data.seal.function.use.UseFunction;
import net.vakror.asm.backpack.BackpackHelper;

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
        BackpackHelper.openBackpackGui(player, player.getItemInHand(hand));
        super.executeUse(level, player, hand);
    }
}
