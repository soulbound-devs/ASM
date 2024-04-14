package net.vakror.soulbound.compat.jei.category;

import net.minecraft.client.gui.GuiGraphics;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.compat.jei.recipe.ModJEIRecipes;
import net.vakror.soulbound.compat.jei.recipe.solidifying.ISoulSolidifyingRecipe;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.soul.SoulType;
import net.vakror.soulbound.util.RegistryUtil;
import org.jetbrains.annotations.NotNull;

public class SoulSolidifyingCategory implements IRecipeCategory<ISoulSolidifyingRecipe> {
    private final IGuiHelper helper;
    private final IDrawable background;

    private final IDrawableAnimated arrow;

    public static final ResourceLocation SOLIDIFIER_TEXTURE = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/soul_solidifier.png");
    public static final ResourceLocation SOLIDIFIER_TEXTURE_BG = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/soul_solidifier_jei.png");

    public SoulSolidifyingCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.drawableBuilder(SOLIDIFIER_TEXTURE_BG, 0, 0, 114, 54)
                .setTextureSize(114, 54)
                .addPadding(0, 0, 0, 0)
                .build();


        IDrawableStatic staticArrow = helper.createDrawable(SOLIDIFIER_TEXTURE, 177, 0, 26, 8);
        arrow = helper.createAnimatedDrawable(staticArrow, 61, IDrawableAnimated.StartDirection.LEFT, false);
    }
    @Override
    public RecipeType<ISoulSolidifyingRecipe> getRecipeType() {
        return ModJEIRecipes.SOUL_SOLIDIFYING;
    }

    @Override
    public void draw(ISoulSolidifyingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 65, 41);

        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("soulbound.compat.jei.solidifying");
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ISoulSolidifyingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).setFluidRenderer(100, true, 16, 46).setSlotName("Soul").addFluidStack(recipe.getSoulType().equals(SoulType.NORMAL) ? ModSoul.SOURCE_SOUL.get(): ModSoul.SOURCE_DARK_SOUL.get(), 100);
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 37).setSlotName("Tungsten").addItemStack(new ItemStack(recipe.getIngotItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 97, 37).setSlotName("Result").addItemStack(recipe.getOutput());
    }
}
