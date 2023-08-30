package net.vakror.soulbound.mod.datagen;

import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.vakror.soulbound.mod.SoulboundMod;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataGeneratorHelper<T> {
    private final ResourceKey<Registry<T>> registryKey;
    private final GatherDataEvent event;
    private final String modid;
    private final Map<ResourceLocation, T> entries = new LinkedHashMap<>();

    public DataGeneratorHelper(ResourceKey<Registry<T>> registryKey, @Nullable GatherDataEvent event, String modid) {
        this.registryKey = registryKey;
        this.event = event;
        this.modid = modid;
    }

    public DataGeneratorHelper<T> add(String location, T entry) {
        entries.put(new ResourceLocation(modid, location), entry);
        return this;
    }

    public void end() {
        if (event == null) {
            throw new IllegalStateException("If GatherDataEvent passed in ctor is null, end must be called with the event as an argument!");
        }
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
                event.getGenerator(), event.getExistingFileHelper(), SoulboundMod.MOD_ID,
                RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltinRegistries.REGISTRY)),
                registryKey,
                entries));
    }

    public void end(GatherDataEvent event) {
        if (event == null && this.event == null) {
            throw new IllegalStateException("GatherDataEvent cannot be both null in ctor and end parameters!");
        }
        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
                event.getGenerator(), event.getExistingFileHelper(), SoulboundMod.MOD_ID,
                RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.fromRegistryOfRegistries(BuiltinRegistries.REGISTRY)),
                registryKey,
                entries));
    }
}
