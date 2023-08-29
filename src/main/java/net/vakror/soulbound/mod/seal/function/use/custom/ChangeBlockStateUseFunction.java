package net.vakror.soulbound.mod.seal.function.use.custom;

import com.google.gson.JsonObject;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.mod.seal.function.use.UseFunction;

import java.nio.file.Path;

public class ChangeBlockStateUseFunction extends UseFunction {
    public ChangeBlockStateUseFunction(JsonObject functionObject, Path path) {
        super(functionObject, path);
    }

    @Override
    public void readFromJson(JsonObject function, Path path) {
        
    }

    @Override
    public void executeUseOn(UseOnContext context) {

    }
}
