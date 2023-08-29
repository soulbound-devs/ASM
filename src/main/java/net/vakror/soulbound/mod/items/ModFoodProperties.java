package net.vakror.soulbound.mod.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties CORRUPTED_BERRY = new FoodProperties.Builder().alwaysEat().fast().saturationMod(1f).nutrition(2).effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 60), 1).build();
}
