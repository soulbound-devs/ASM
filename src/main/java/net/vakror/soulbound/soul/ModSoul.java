package net.vakror.soulbound.soul;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.items.ModItems;

public class ModSoul {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SoulboundMod.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_SOUL = FLUIDS.register("soul",
            () -> new ForgeFlowingFluid.Source(ModSoul.SOUL_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_SOUL = FLUIDS.register("flowing_soul",
            () -> new ForgeFlowingFluid.Flowing(ModSoul.SOUL_PROPERTIES));

    public static final RegistryObject<FlowingFluid> SOURCE_DARK_SOUL = FLUIDS.register("dark_soul",
            () -> new ForgeFlowingFluid.Source(ModSoul.DARK_SOUL_PROPERTIES));

    public static final RegistryObject<FlowingFluid> FLOWING_DARK_SOUL = FLUIDS.register("flowing_dark_soul",
            () -> new ForgeFlowingFluid.Flowing(ModSoul.DARK_SOUL_PROPERTIES));


    public static final ForgeFlowingFluid.Properties SOUL_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModSoulTypes.SOUL, SOURCE_SOUL, FLOWING_SOUL).slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SOUL_FLUID_BLOCK).bucket(ModItems.SOUL_BUCKET);

    public static final ForgeFlowingFluid.Properties DARK_SOUL_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModSoulTypes.DARK_SOUL, SOURCE_DARK_SOUL, FLOWING_DARK_SOUL).slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.DARK_SOUL_FLUID_BLOCK).bucket(ModItems.DARK_SOUL_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
