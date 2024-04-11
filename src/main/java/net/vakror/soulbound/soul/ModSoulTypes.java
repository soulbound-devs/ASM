package net.vakror.soulbound.soul;

import net.neoforged.bus.api.IEventBus;
import org.joml.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.SoulboundMod;

public class ModSoulTypes {
    public static final ResourceLocation SOUL_STILL = new ResourceLocation(SoulboundMod.MOD_ID, "block/soul_still");
    public static final ResourceLocation SOUL_FLOWING = new ResourceLocation(SoulboundMod.MOD_ID, "block/soul_flowing");
    public static final ResourceLocation DARK_SOUL_STILL = new ResourceLocation(SoulboundMod.MOD_ID, "block/dark_soul_still");
    public static final ResourceLocation DARK_SOUL_FLOWING = new ResourceLocation(SoulboundMod.MOD_ID, "block/dark_soul_flowing");
    public static final ResourceLocation SOUL_OVERLAY = new ResourceLocation(SoulboundMod.MOD_ID, "gui/in_soul_overlay");
    public static final ResourceLocation DARK_SOUL_OVERLAY = new ResourceLocation(SoulboundMod.MOD_ID, "gui/in_dark_soul_overlay");

    public static final DeferredRegister<FluidType> SOUL_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, SoulboundMod.MOD_ID);

    public static final DeferredHolder<FluidType, BaseSoulType> SOUL = register("soul", FluidType.Properties.create().canHydrate(false).canDrown(true).canSwim(false).canExtinguish(true).canPushEntity(false)
            .canConvertToSource(false).fallDistanceModifier(1).density(15).lightLevel(2).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK), SOUL_STILL, SOUL_FLOWING, SOUL_OVERLAY, 121, 98, 140);

    public static final DeferredHolder<FluidType, BaseSoulType> DARK_SOUL = register("dark_soul", FluidType.Properties.create().canHydrate(false).canDrown(true).canSwim(false).canExtinguish(true).canPushEntity(false)
            .canConvertToSource(false).fallDistanceModifier(1).density(15).lightLevel(2).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK), DARK_SOUL_STILL, DARK_SOUL_FLOWING, DARK_SOUL_OVERLAY, 27, 24, 44);

    private static DeferredHolder<FluidType, BaseSoulType> register(String name, FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlay, float fogR, float fogG, float fogB) {
        return SOUL_TYPES.register(name, () -> new BaseSoulType(stillTexture, flowingTexture, overlay,
                0xA0FFFFFF, new Vector3f(fogR / 255, fogG / 255, fogB / 255), properties));
    }

    public static void register(IEventBus eventBus) {
        SOUL_TYPES.register(eventBus);
    }
}
