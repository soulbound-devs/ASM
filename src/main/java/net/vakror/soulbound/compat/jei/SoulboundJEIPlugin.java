package net.vakror.soulbound.compat.jei;

import dev.architectury.platform.Platform;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.dungeon.items.ModDungeonItems;
import net.vakror.soulbound.compat.jei.category.SoulExtractingCategory;
import net.vakror.soulbound.compat.jei.category.SoulSolidifyingCategory;
import net.vakror.soulbound.compat.jei.category.WandImbuingCategory;
import net.vakror.soulbound.compat.jei.recipe.ModJEIRecipes;
import net.vakror.soulbound.compat.jei.recipe.extracting.ISoulExtractingRecipe;
import net.vakror.soulbound.compat.jei.recipe.extracting.SoulExtractingRecipe;
import net.vakror.soulbound.compat.jei.recipe.imbuing.IWandImbuingRecipe;
import net.vakror.soulbound.compat.jei.recipe.imbuing.WandImbuingRecipe;
import net.vakror.soulbound.compat.jei.recipe.solidifying.ISoulSolidifyingRecipe;
import net.vakror.soulbound.compat.jei.recipe.solidifying.SoulSolidifyingRecipe;
import net.vakror.soulbound.compat.jei.transfer.WandImbuingRecipeTransferInfo;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.items.custom.SealableItem;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.Tooltip;
import net.vakror.soulbound.soul.SoulType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@JeiPlugin
public class SoulboundJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ModJEIRecipes.WAND_IMBUING, getAllWandImbuingRecipes());
        registration.addRecipes(ModJEIRecipes.SOUL_SOLIDIFYING, getSoulSolidifyingRecipes());
        registration.addRecipes(ModJEIRecipes.SOUL_EXTRACTING, getSoulExtractingRecipes());
        registration.addItemStackInfo(ModDungeonItems.KEY.get().getDefaultInstance(), new Tooltip.TooltipComponentBuilder().addPartWithNewline("The key is an item important in the progression of ASM. It is used for only one purpose, that being the unlocking of a Keystone", Tooltip.TooltipComponentBuilder.ColorCode.GREEN).build().getTooltip());
        registration.addItemStackInfo(ModDungeonBlocks.DUNGEON_KEY_BLOCK.get().asItem().getDefaultInstance(), new Tooltip.TooltipComponentBuilder().addPartWithNewline("The keystone is vital to finishing ASM. When right-clicked with a key, it will unlock and teleport you to a dangerous, but rewarding dimension known as the dungeon. Blocks cannot be placed and broken (except for chests and their variants) in dungeons. Dungeons come in two forms, as shown by the keystone block texture. Stable and Unstable. Stable dungeons are easier and have worse loot, but once beaten, the player can enter again. Once a stable dungeon is fully beaten, the player will be able to place and break blocks inside it. Unstable dungeons are harder and have better loot. Once an unstable dungeon is beaten, the player will be teleported out and cannot enter again. There are eight spawning locations in a dungeon. When entering a dungeon, one will have a random size. The maximum depends on how many dungeons a player has finished before. These sizes are: 50, 75, 100, and 125. In addition to this. Each dungeon has a different amount of \"Layers\", depending on the amount of dungeons of the same size a player has beaten, the dungeon will have more layers. Each new layer can be accessed by killing the boss and all of his minions of the layer below. In a stable dungeon the player can exit by returning at lowest level of the dungeon, where there is a \"Return\" block. Right-Clicking this will teleport the player to the world spawn of the overworld. Each dungeon size and layer will have different challenges and rewards. Each one will have \"Rooms\". These are where the loot and dangerous mobs are found.         All rooms need to be beaten to start the boss fight.", Tooltip.TooltipComponentBuilder.ColorCode.GREEN).build().getTooltip());
        IModPlugin.super.registerRecipes(registration);
    }

    private List<ISoulExtractingRecipe> getSoulExtractingRecipes() {
        List<ISoulExtractingRecipe> recipes = new ArrayList<>();
        recipes.add(SoulExtractingRecipe.create());
        return recipes;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new WandImbuingCategory(guiHelper));
        registration.addRecipeCategories(new SoulSolidifyingCategory(guiHelper));
        registration.addRecipeCategories(new SoulExtractingCategory(guiHelper));
        IModPlugin.super.registerCategories(registration);
    }

    private List<IWandImbuingRecipe> getAllWandImbuingRecipes() {
        List<IWandImbuingRecipe> recipes = new ArrayList<>();
        SealRegistry.allSeals.keySet().forEach((sealId -> recipes.add(WandImbuingRecipe.create(SealRegistry.sealItems.get(sealId).get()))));

        return recipes;
    }

    private List<ISoulSolidifyingRecipe> getSoulSolidifyingRecipes() {
        List<ISoulSolidifyingRecipe> recipes = new ArrayList<>();
        recipes.add(SoulSolidifyingRecipe.create(ModItems.TUNGSTEN_INGOT.get(), SoulType.NORMAL));
        recipes.add(SoulSolidifyingRecipe.create(ModItems.TUNGSTEN_INGOT.get(), SoulType.DARK));
        return recipes;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new WandImbuingRecipeTransferInfo());
        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WAND_IMBUING_TABLE.get()), ModJEIRecipes.WAND_IMBUING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SOUL_SOLIDIFIER.get()), ModJEIRecipes.SOUL_SOLIDIFYING);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registerSubtypes(registration, ModItems.ITEMS_REGISTRY.getEntries());
        IModPlugin.super.registerItemSubtypes(registration);
    }

    private static final IIngredientSubtypeInterpreter<ItemStack> WAND_INTERPRETER = (stack, context) -> {
        if (stack.hasTag()) {
            StringBuilder nbtRepresentation = new StringBuilder();
            stack.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent(seal -> {
                if (seal.getActiveSeal() != null) {
                    nbtRepresentation.append("active-").append(seal.getActiveSeal().getId());
                } else if (seal.getAmplifyingSeals() != null && !seal.getAmplifyingSeals().isEmpty()) {
                    try {
                        Stream<ISeal> stream = seal.getAmplifyingSeals().stream();
                        if (stream != null) {
                            Optional<ISeal> optional = stream.findFirst();
                            optional.ifPresent(iSeal -> nbtRepresentation.append("amplifying-").append(iSeal.getId()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return nbtRepresentation.toString();
        }
        return IIngredientSubtypeInterpreter.NONE;
    };

    private void registerSubtypes(ISubtypeRegistration registration, Collection<DeferredHolder<Item, ? extends Item>> entries) {
        for (DeferredHolder<Item, ? extends Item> RegistrySupplier: entries) {
            Item item = RegistrySupplier.get();
            if (item instanceof SealableItem) {
                registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, item, WAND_INTERPRETER);
            }
        }
    }
}
