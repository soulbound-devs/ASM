package net.vakror.asm.compat.jei.recipe;

import mezz.jei.api.recipe.RecipeType;
import net.vakror.asm.ASMMod;
import net.vakror.asm.compat.jei.recipe.extracting.ISoulExtractingRecipe;
import net.vakror.asm.compat.jei.recipe.imbuing.IWandImbuingRecipe;
import net.vakror.asm.compat.jei.recipe.solidifying.ISoulSolidifyingRecipe;

public class ModJEIRecipes {

    public static final RecipeType<IWandImbuingRecipe> WAND_IMBUING =
            RecipeType.create(ASMMod.MOD_ID, "imbuing", IWandImbuingRecipe.class);
    public static final RecipeType<ISoulSolidifyingRecipe> SOUL_SOLIDIFYING =
            RecipeType.create(ASMMod.MOD_ID, "solidifying", ISoulSolidifyingRecipe.class);
    public static final RecipeType<ISoulExtractingRecipe> SOUL_EXTRACTING =
            RecipeType.create(ASMMod.MOD_ID, "extracting", ISoulExtractingRecipe.class);
}
