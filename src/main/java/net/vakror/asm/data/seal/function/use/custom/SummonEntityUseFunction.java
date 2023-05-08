package net.vakror.asm.data.seal.function.use.custom;

import com.google.gson.JsonObject;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.data.seal.function.use.UseFunction;

import java.nio.file.Path;

public class SummonEntityUseFunction extends UseFunction {
    public SummonEntityUseFunction(JsonObject functionObject, Path path) {
        super(functionObject, path);
    }

    @Override
    public void readFromJson(JsonObject function, Path path) {
        
    }

    @Override
    public void executeUse(UseOnContext context) {

    }
}
