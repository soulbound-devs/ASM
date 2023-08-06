package net.vakror.soulbound.model.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;

import java.util.Map;

public class ActiveSealModels {
    public static Map<String, ResourceLocation> MODELS;

    public static void init() {
        ImmutableMap.Builder<String, ResourceLocation> builder = new ImmutableMap.Builder<>();
        builder.put("pickaxing", new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/pickaxing"));
        builder.put("hoeing", new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/pickaxing"));
        builder.put("shoveling", new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/hoeing"));
        builder.put("swording", new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/shovelling"));
        builder.put("axing", new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/swording"));
        builder.put("scythe", new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/scythe"));
        MODELS = builder.build();
    }
}
