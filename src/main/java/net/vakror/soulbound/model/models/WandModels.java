package net.vakror.soulbound.model.models;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class WandModels {
    public static Map<String, ResourceLocation> MODELS = new HashMap<>();

    public static void registerModel(String name, ResourceLocation location) {
        MODELS.put(name, location);
    }

    public static Map<String, ResourceLocation> getModels() {
        return MODELS;
    }
}
