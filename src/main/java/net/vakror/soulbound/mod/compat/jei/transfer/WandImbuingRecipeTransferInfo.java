package net.vakror.soulbound.mod.compat.jei.transfer;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.vakror.soulbound.mod.compat.jei.recipe.imbuing.IWandImbuingRecipe;
import net.vakror.soulbound.mod.compat.jei.recipe.ModJEIRecipes;
import net.vakror.soulbound.mod.screen.WandImbuingMenu;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class WandImbuingRecipeTransferInfo implements IRecipeTransferInfo<WandImbuingMenu, IWandImbuingRecipe> {

    @Override
    public @NotNull Class getContainerClass() {
        return WandImbuingMenu.class;
    }

    @Override
    public @NotNull Optional<MenuType<WandImbuingMenu>> getMenuType() {
        return Optional.empty();
    }

    @Override
    public @NotNull RecipeType<IWandImbuingRecipe> getRecipeType() {
        return ModJEIRecipes.WAND_IMBUING;
    }

    @Override
    public boolean canHandle(@NotNull WandImbuingMenu container, @NotNull IWandImbuingRecipe recipe) {
        return true;
    }

    @Override
    public @NotNull List<Slot> getRecipeSlots(@NotNull WandImbuingMenu container, @NotNull IWandImbuingRecipe recipe) {
        return container.getInvSlots();
    }

    @Override
    public @NotNull List<Slot> getInventorySlots(@NotNull WandImbuingMenu container, @NotNull IWandImbuingRecipe recipe) {
        return container.getPlayerInvSlots();
    }
}
