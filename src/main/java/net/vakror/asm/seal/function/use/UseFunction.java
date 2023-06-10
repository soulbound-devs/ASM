package net.vakror.asm.seal.function.use;

import com.google.gson.JsonObject;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.nio.file.Path;

public class UseFunction {
    public String id;
    public UseActionType type;

    public UseFunction(JsonObject function, Path path) {
        readFromJson(function, path);
    }

    public void readFromJson(JsonObject function, Path path) {
        this.id = function.get("id").getAsString();
        this.type = UseActionType.valueOf(function.get("action").getAsString().toUpperCase());
    }

    public void executeUseOn(UseOnContext context) {

    }

    public void executeUse(Level level, Player player, InteractionHand hand) {

    }
}
