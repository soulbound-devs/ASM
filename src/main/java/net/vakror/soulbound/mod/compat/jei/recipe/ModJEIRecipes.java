package net.vakror.soulbound.mod.compat.jei.recipe;

import mezz.jei.api.recipe.RecipeType;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.jei.recipe.extracting.ISoulExtractingRecipe;
import net.vakror.soulbound.mod.compat.jei.recipe.imbuing.IWandImbuingRecipe;
import net.vakror.soulbound.mod.compat.jei.recipe.solidifying.ISoulSolidifyingRecipe;

public class ModJEIRecipes {

    public static final RecipeType<IWandImbuingRecipe> WAND_IMBUING =
            RecipeType.create(SoulboundMod.MOD_ID, "imbuing", IWandImbuingRecipe.class);
    public static final RecipeType<ISoulSolidifyingRecipe> SOUL_SOLIDIFYING =
            RecipeType.create(SoulboundMod.MOD_ID, "solidifying", ISoulSolidifyingRecipe.class);
    public static final RecipeType<ISoulExtractingRecipe> SOUL_EXTRACTING =
            RecipeType.create(SoulboundMod.MOD_ID, "extracting", ISoulExtractingRecipe.class);
}
