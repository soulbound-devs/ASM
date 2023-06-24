package net.vakror.asm.compat.jei.recipe.imbuing;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.vakror.asm.items.custom.seals.SealItem;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * A recipe in JEI that displays wand imbuing stuff.
 */
public interface IWandImbuingRecipe {
    /**
     * The input ingredients for the recipe.
     */
    @Unmodifiable
    List<Ingredient> getIngredients();

    /**
     * The wand
     */
    @Unmodifiable
    ItemStack getWand();

    /**
     * The wand
     */
    @Unmodifiable
    SealItem getSeal();

    /**
     * The output wand
     */
    @Unmodifiable
    ItemStack getOutput();
}
