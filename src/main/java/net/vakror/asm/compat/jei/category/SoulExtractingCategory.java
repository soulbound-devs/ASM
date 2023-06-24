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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.compat.jei.recipe.ModJEIRecipes;
import net.vakror.asm.compat.jei.recipe.extracting.ISoulExtractingRecipe;
import org.jetbrains.annotations.NotNull;

public class SoulExtractingCategory implements IRecipeCategory<ISoulExtractingRecipe> {
    private final IGuiHelper helper;
    private final IDrawable background;
    private final IDrawableStatic staticArrow;

    private final IDrawableAnimated arrow;

    public static final ResourceLocation EXTRACTOR_TEXTURE_BG = new ResourceLocation(ASMMod.MOD_ID, "textures/gui/soul_extractor.png");

    public SoulExtractingCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.drawableBuilder(EXTRACTOR_TEXTURE_BG, 3, 20, 153, 48)
                .addPadding(0, 0, 0, 0)
                .build();


        staticArrow = helper.createDrawable(EXTRACTOR_TEXTURE_BG, 177, 0, 26, 8);
        arrow = helper.createAnimatedDrawable(staticArrow, 13, IDrawableAnimated.StartDirection.LEFT, false);
    }
    @Override
    public RecipeType<ISoulExtractingRecipe> getRecipeType() {
        return ModJEIRecipes.SOUL_EXTRACTING;
    }

    @Override
    public void draw(ISoulExtractingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 24, 18);
        arrow.draw(guiGraphics, 104, 18);

        drawSoul(guiGraphics, 2, 100, false);
        drawSoul(guiGraphics, 76, 100, true);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("asm.compat.jei.extracting");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return helper.createDrawableItemStack(new ItemStack(ModBlocks.SOUL_SOLIDIFIER.get()));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ISoulExtractingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 1).setFluidRenderer(1000, true, 16, 46).setSlotName("Soul Output").addFluidStack(recipe.getSoulFluid().getFluid(), 1000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 136, 1).setFluidRenderer(1000, true, 16, 46).setSlotName("Dark Soul Output").addFluidStack(recipe.getDarkSoulFluid().getFluid(), 1000);
    }

    protected void drawSoul(GuiGraphics guiGraphics, int x, int amount, boolean isDarkSoul) {
        if (amount > 0) {
            Component soulString = Component.literal(amount + (isDarkSoul ? " Dark ": " ") + "Soul");
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            guiGraphics.pose().last().pose().scale(0.5f, 0.5f, 0.5f);
            guiGraphics.drawString(fontRenderer, soulString, x * 2 - 2, 38, 0xFF808080, false);
            guiGraphics.pose().last().pose().scale(2, 2, 2);
        }
    }
}
