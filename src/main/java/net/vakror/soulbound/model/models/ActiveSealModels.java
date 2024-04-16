package net.vakror.soulbound.model.models;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ActiveSealModels {
    public static Map<ResourceLocation, ResourceLocation> MODELS = new HashMap<>();

    public static void registerModel(ResourceLocation name, ResourceLocation location) {
        MODELS.put(name, location);
    }

    public static Map<ResourceLocation, ResourceLocation> getModels() {
        return MODELS;
    }
}
