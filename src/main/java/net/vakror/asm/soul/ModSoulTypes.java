package net.vakror.asm.soul;

import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;

public class ModSoulTypes {
    public static ResourceLocation SOUL_STILL = new ResourceLocation(ASMMod.MOD_ID, "block/soul_still");
    public static ResourceLocation SOUL_FLOWING = new ResourceLocation(ASMMod.MOD_ID, "block/soul_flowing");
    public static ResourceLocation DARK_SOUL_STILL = new ResourceLocation(ASMMod.MOD_ID, "block/dark_soul_still");
    public static ResourceLocation DARK_SOUL_FLOWING = new ResourceLocation(ASMMod.MOD_ID, "block/dark_soul_flowing");
    public static ResourceLocation SOUL_OVERLAY = new ResourceLocation(ASMMod.MOD_ID, "gui/in_soul_overlay");
    public static ResourceLocation DARK_SOUL_OVERLAY = new ResourceLocation(ASMMod.MOD_ID, "gui/in_dark_soul_overlay");

    public static final DeferredRegister<FluidType> SOUL_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ASMMod.MOD_ID);

    public static final RegistryObject<FluidType> SOUL = register("soul", FluidType.Properties.create().canHydrate(false).canDrown(true).canSwim(false).canExtinguish(true).canPushEntity(false)
            .canConvertToSource(false).fallDistanceModifier(1).density(15).lightLevel(2).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK), SOUL_STILL, SOUL_FLOWING, SOUL_OVERLAY, 121, 98, 140);

    public static final RegistryObject<FluidType> DARK_SOUL = register("dark_soul", FluidType.Properties.create().canHydrate(false).canDrown(true).canSwim(false).canExtinguish(true).canPushEntity(false)
            .canConvertToSource(false).fallDistanceModifier(1).density(15).lightLevel(2).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK), DARK_SOUL_STILL, DARK_SOUL_FLOWING, DARK_SOUL_OVERLAY, 27, 24, 44);

    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlay, float fogR, float fogG, float fogB) {
        return SOUL_TYPES.register(name, () -> new BaseSoulType(stillTexture, flowingTexture, overlay,
                0xA0FFFFFF, new Vector3f(fogR / 255, fogG / 255, fogB / 255), properties));
    }

    public static void register(IEventBus eventBus) {
        SOUL_TYPES.register(eventBus);
    }
}
