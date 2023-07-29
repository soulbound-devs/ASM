package net.vakror.soulbound.compat.hammerspace.dimension;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.vakror.soulbound.SoulboundMod;


public class Dimensions {
    public static final ResourceKey<Level> DUNGEON_DIM_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(SoulboundMod.MOD_ID, "dungeon"));
    public static final ResourceKey<DimensionType> DUNGEON_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, DUNGEON_DIM_KEY.location());

    public static void register() {
        SoulboundMod.LOGGER.info("Dimensions are being registered for soulbound");
    }

}
