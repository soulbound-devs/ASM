package net.vakror.asm.world.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.vakror.asm.ASMMod;


public class Dimensions {
    public static final ResourceKey<Level> DUNGEON_DIM_KEY = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(ASMMod.MOD_ID, "dungeon"));
    public static final ResourceKey<DimensionType> DUNGEON_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, DUNGEON_DIM_KEY.location());

    public static void register() {
        ASMMod.LOGGER.info("Dimensions are being registered for asm");
    }

}
