package net.vakror.soulbound.model.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;

import java.util.Map;

public class ActiveSealModels {
    public static Map<String, ResourceLocation> MODELS;

    public static void init() {
        ImmutableMap.Builder<String, ResourceLocation> builder = new ImmutableMap.Builder<>();
        builder.put("pickaxing", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        builder.put("hoeing", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        builder.put("shoveling", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        builder.put("swording", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        builder.put("axing", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        builder.put("scythe", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        MODELS = builder.build();
    }
}
