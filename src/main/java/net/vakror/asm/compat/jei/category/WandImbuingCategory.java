package net.vakror.asm.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.compat.jei.recipe.ModJEIRecipes;
import net.vakror.asm.compat.jei.recipe.imbuing.IWandImbuingRecipe;
import net.vakror.asm.items.ModItems;
import org.jetbrains.annotations.NotNull;

public class WandImbuingCategory implements IRecipeCategory<IWandImbuingRecipe> {
    private final IGuiHelper helper;
    private final IDrawable background;
    private final IDrawableStatic staticArrow;
    private final IDrawableStatic staticSoul;

    private final IDrawableAnimated arrow;
    private final IDrawableAnimated soul;

    public static final ResourceLocation IMBUER_TEXTURE = new ResourceLocation(ASMMod.MOD_ID, "textures/gui/imbuer_gui.png");

    public WandImbuingCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.drawableBuilder(IMBUER_TEXTURE, 17, 32, 146, 37)
                .addPadding(0, 0, 0, 0)
                .build();


        staticArrow = helper.createDrawable(IMBUER_TEXTURE, 177, 38, 18, 8);
        arrow = helper.createAnimatedDrawable(staticArrow, 108, IDrawableAnimated.StartDirection.LEFT, false);

        staticSoul = helper.createDrawable(IMBUER_TEXTURE, 177, 0, 13, 16);
        soul = helper.createAnimatedDrawable(staticSoul, 200, IDrawableAnimated.StartDirection.TOP, true);
    }
    @Override
    public RecipeType<IWandImbuingRecipe> getRecipeType() {
        return ModJEIRecipes.WAND_IMBUING;
    }

    @Override
    public void draw(IWandImbuingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 101, 5);
        soul.draw(guiGraphics, 2, 0);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("asm.compat.jei.wand_imbuing");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return helper.createDrawableItemStack(new ItemStack(ModBlocks.WAND_IMBUING_TABLE.get()));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull IWandImbuingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 20).setSlotName("Soul").addItemStack(new ItemStack(ModItems.SOUL.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 1).setSlotName("Wand").addItemStack(recipe.getWand());
        builder.addSlot(RecipeIngredientRole.INPUT, 74, 1).setSlotName("Seal").addItemStack(new ItemStack(recipe.getSeal()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 1).setSlotName("Result").addItemStack(recipe.getOutput());
    }
}
