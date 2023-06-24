package net.vakror.asm.compat.jei.transfer;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.vakror.asm.compat.jei.recipe.ModJEIRecipes;
import net.vakror.asm.compat.jei.recipe.solidifying.ISoulSolidifyingRecipe;
import net.vakror.asm.screen.SoulSolidifierMenu;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SoulSolidifyingRecipeTransferInfo implements IRecipeTransferInfo<SoulSolidifierMenu, ISoulSolidifyingRecipe> {

    @Override
    public @NotNull Class getContainerClass() {
        return SoulSolidifierMenu.class;
    }

    @Override
    public @NotNull Optional<MenuType<SoulSolidifierMenu>> getMenuType() {
        return Optional.empty();
    }

    @Override
    public @NotNull RecipeType<ISoulSolidifyingRecipe> getRecipeType() {
        return ModJEIRecipes.SOUL_SOLIDIFYING;
    }

    @Override
    public boolean canHandle(@NotNull SoulSolidifierMenu container, @NotNull ISoulSolidifyingRecipe recipe) {
        return true;
    }

    @Override
    public @NotNull List<Slot> getRecipeSlots(@NotNull SoulSolidifierMenu container, @NotNull ISoulSolidifyingRecipe recipe) {
        return container.getInvSlots();
    }

    @Override
    public @NotNull List<Slot> getInventorySlots(@NotNull SoulSolidifierMenu container, @NotNull ISoulSolidifyingRecipe recipe) {
        return container.getPlayerInvSlots();
    }
}
