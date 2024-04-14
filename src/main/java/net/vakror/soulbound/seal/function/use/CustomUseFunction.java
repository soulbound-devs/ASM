package net.vakror.soulbound.seal.function.use;

import com.google.gson.JsonObject;

import java.nio.file.Path;

public class CustomUseFunction extends UseFunction {
    public UseActionType type;

    public CustomUseFunction(JsonObject function, Path path) {
        readFromJson(function, path);
    }

    public CustomUseFunction() {
    }

    @Override
    public void readFromJson(JsonObject function, Path path) {
        this.id = function.get("id").getAsString();
        this.type = UseActionType.valueOf(function.get("action").getAsString().toUpperCase());
    }
}
