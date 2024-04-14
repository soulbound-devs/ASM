package net.vakror.soulbound.util;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RegistryUtil {
    public static <T> T getRegistryObjectByName(ResourceKey<Registry<T>> registry, ResourceLocation name) {
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY).registryOrThrow(registry).get(name);
    }
}
