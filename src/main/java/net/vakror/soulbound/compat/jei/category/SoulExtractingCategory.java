package net.vakror.soulbound.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.compat.jei.recipe.ModJEIRecipes;
import net.vakror.soulbound.compat.jei.recipe.extracting.ISoulExtractingRecipe;
import org.jetbrains.annotations.NotNull;

public class SoulExtractingCategory implements IRecipeCategory<ISoulExtractingRecipe> {
    private final IGuiHelper helper;
    private final IDrawable background;
    private final IDrawableStatic staticArrow;

    private final IDrawableAnimated arrow;

    public static final ResourceLocation EXTRACTOR_TEXTURE_BG = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/soul_extractor.png");

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
    public void draw(ISoulExtractingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrices, double mouseX, double mouseY) {
        arrow.draw(matrices, 24, 41);
        arrow.draw(matrices, 104, 42);

        drawSoul(guiGraphics, 2, 100, false);
        drawSoul(guiGraphics, 76, 100, true);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, matrices, mouseX, mouseY);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("soulbound.compat.jei.extracting");
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

    protected void drawSoul(PoseStack guiGraphics, int x, int amount, boolean isDarkSoul) {
        if (amount > 0) {
            Component soulString = Component.literal(amount + (isDarkSoul ? " Dark ": " ") + "Soul");
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            guiGraphics.scale(0.5f, 0.5f, 0.5f);
            GuiComponent.drawString(guiGraphics, fontRenderer, soulString, x * 2 - 2, 38, 0xFF808080);
            guiGraphics.scale(2, 2, 2);
        }
    }
}
