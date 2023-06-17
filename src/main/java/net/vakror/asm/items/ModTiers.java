package net.vakror.asm.items;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    DIAMOND_LIKE(3, 2375, 8.0F, 0.0F, 0, () -> Ingredient.EMPTY);

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModTiers(int miningLevel, int durability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairable) {
        this.level = miningLevel;
        this.uses = durability;
        this.speed = miningSpeed;
        this.damage = attackDamage;
        this.enchantmentValue = enchantability;
        this.repairIngredient = new LazyLoadedValue<>(repairable);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
