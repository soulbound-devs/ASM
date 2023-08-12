package net.vakror.soulbound.model.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;

import java.util.Map;

public class WandModels {
    public static Map<String, ResourceLocation> MODELS;
    public static Map<String, Integer> COLORS;

    public static void init() {
        ImmutableMap.Builder<String, ResourceLocation> builder = new ImmutableMap.Builder<>();
        builder.put("ancient_oak", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/wand/ancient_oak/base.obj"));
        MODELS = builder.build();
        COLORS = new ImmutableMap.Builder<String, Integer>().build();
    }
}
