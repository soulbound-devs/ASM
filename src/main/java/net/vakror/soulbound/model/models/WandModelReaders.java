package net.vakror.soulbound.model.models;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.model.wand.api.IWandModelReader;

import java.util.HashMap;
import java.util.Map;

public class WandModelReaders {
    public static final Map<ResourceLocation, IWandModelReader> READERS = new HashMap<>();
}
