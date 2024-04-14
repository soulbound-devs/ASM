package net.vakror.soulbound.compat.jei.category;

import net.minecraft.client.gui.GuiGraphics;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.compat.jei.recipe.ModJEIRecipes;
import net.vakror.soulbound.compat.jei.recipe.extracting.ISoulExtractingRecipe;
import net.vakror.soulbound.util.RegistryUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SoulExtractingCategory implements IRecipeCategory<ISoulExtractingRecipe> {
    private final IGuiHelper helper;
    private final IDrawable background;
    private final IDrawable soulBarFull;
    private final IDrawable darkSoulBarFull;
    private final IDrawableStatic staticArrow;

    public static final ResourceLocation EXTRACTOR_TEXTURE_BG = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/soul_extractor.png");

    public static final ResourceLocation SOUL_BAR_FULL = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/soul_bar_full.png");

    public static final ResourceLocation DARK_SOUL_BAR_FULL = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/dark_soul_bar_full.png");

    public SoulExtractingCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.drawableBuilder(EXTRACTOR_TEXTURE_BG, 3, 20, 153, 48)
                .addPadding(25, 25, 0, 0)
                .build();


        this.soulBarFull = helper.drawableBuilder(SOUL_BAR_FULL, 0, 0, 28, 56)
                .addPadding(0, 0, 0, 0)
                .setTextureSize(28, 56)
                .build();

        this.darkSoulBarFull = helper.drawableBuilder(DARK_SOUL_BAR_FULL, 0, 0, 28, 56)
                .addPadding(0, 0, 0, 0)
                .setTextureSize(28, 56)
                .build();

        staticArrow = helper.createDrawable(EXTRACTOR_TEXTURE_BG, 177, 0, 26, 8);
    }
    @Override
    public RecipeType<ISoulExtractingRecipe> getRecipeType() {
        return ModJEIRecipes.SOUL_EXTRACTING;
    }

    @Override
    public void draw(ISoulExtractingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        staticArrow.draw(graphics, 24, 41);
        staticArrow.draw(graphics, 104, 42);

        graphics.pose().pushPose();
        graphics.pose().scale(0.75f, 0.75f, 0.75f);
        soulBarFull.draw(graphics, 0, 35);
        darkSoulBarFull.draw(graphics, 110, 35);
        graphics.pose().popPose();

        IRecipeCategory.super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);
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
        return helper.createDrawableItemStack(new ItemStack(ModBlocks.SOUL_EXTRACTOR.get()));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ISoulExtractingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 56, 26).setFluidRenderer(1000, true, 16, 46).setSlotName("Soul Output").addFluidStack(recipe.getSoulFluid().getFluid(), 1000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 136, 26).setFluidRenderer(1000, true, 16, 46).setSlotName("Dark Soul Output").addFluidStack(recipe.getDarkSoulFluid().getFluid(), 1000);
    }

    @Override
    public List<Component> getTooltipStrings(ISoulExtractingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isMouseOver(0, 35, mouseX, mouseY, soulBarFull.getWidth() / 2, soulBarFull.getHeight() / 2)) {
            return List.of(Component.literal("100 Soul"));
        }
        if (isMouseOver(85, 35, mouseX, mouseY, darkSoulBarFull.getWidth() / 2, darkSoulBarFull.getHeight() / 2)) {
            return List.of(Component.literal("100 Dark Soul"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    public boolean isMouseOver(int x, int y, double mouseX, double mouseY, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
