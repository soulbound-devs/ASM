package net.vakror.soulbound.soul;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.items.ModItems;

public class ModSoul {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, SoulboundMod.MOD_ID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE_SOUL = FLUIDS.register("soul",
            () -> new BaseFlowingFluid.Source(ModSoul.SOUL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_SOUL = FLUIDS.register("flowing_soul",
            () -> new BaseFlowingFluid.Flowing(ModSoul.SOUL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE_DARK_SOUL = FLUIDS.register("dark_soul",
            () -> new BaseFlowingFluid.Source(ModSoul.DARK_SOUL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_DARK_SOUL = FLUIDS.register("flowing_dark_soul",
            () -> new BaseFlowingFluid.Flowing(ModSoul.DARK_SOUL_PROPERTIES));


    public static final BaseFlowingFluid.Properties SOUL_PROPERTIES = new BaseFlowingFluid.Properties(
            ModSoulTypes.SOUL, SOURCE_SOUL, FLOWING_SOUL).slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SOUL_FLUID_BLOCK).bucket(ModItems.SOUL_BUCKET);

    public static final BaseFlowingFluid.Properties DARK_SOUL_PROPERTIES = new BaseFlowingFluid.Properties(
            ModSoulTypes.DARK_SOUL, SOURCE_DARK_SOUL, FLOWING_DARK_SOUL).slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.DARK_SOUL_FLUID_BLOCK).bucket(ModItems.DARK_SOUL_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
