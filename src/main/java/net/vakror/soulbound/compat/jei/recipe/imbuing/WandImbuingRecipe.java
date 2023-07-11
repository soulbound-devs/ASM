package net.vakror.soulbound.compat.jei.recipe.imbuing;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.vakror.soulbound.capability.wand.ItemSealProvider;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.seal.SealRegistry;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class WandImbuingRecipe implements IWandImbuingRecipe {

	private final ItemStack wand;
	private final SealItem sealItem;
	private final ItemStack output;

	public static IWandImbuingRecipe create(Item item) {

		SealItem seal = item instanceof SealItem ? (SealItem) item: null;

		if (seal == null) {
			throw new IllegalArgumentException("Item in SealRegistry has to be a seal item!\nId: " + item.toString());
		}

		ItemStack outputWand = new ItemStack(ModItems.WAND.get());
		outputWand.getCapability(ItemSealProvider.SEAL).ifPresent((itemSeal -> {
			switch (seal.getType()) {
				case PASSIVE -> {
					itemSeal.addPassiveSeal(seal.getId());
					itemSeal.setActiveSeal(SealRegistry.passiveSeals.get(seal.getId()), outputWand);
				}
				case OFFENSIVE -> {
					itemSeal.addAttackSeal(seal.getId());
					itemSeal.setActiveSeal(SealRegistry.attackSeals.get(seal.getId()), outputWand);
				}
				case AMPLIFYING -> itemSeal.addAmplifyingSeal(seal.getId());
			}
		}));

		WandImbuingRecipe recipe = new WandImbuingRecipe(new ItemStack(ModItems.WAND.get()), seal, outputWand);

		return recipe;
	}

	public WandImbuingRecipe(ItemStack wand, SealItem sealItem, ItemStack output) {
		this.wand = wand;
		this.sealItem = sealItem;
		this.output = output;
	}

	@Override
	public @Unmodifiable List<Ingredient> getIngredients() {
		List<Ingredient> list = new ArrayList<>();
		list.add(Ingredient.of(wand));
		list.add(Ingredient.of(sealItem));
		return list;
	}

	@Override
	public @Unmodifiable ItemStack getWand() {
		return wand;
	}

	@Override
	public @Unmodifiable SealItem getSeal() {
		return sealItem;
	}

	@Override
	public @Unmodifiable ItemStack getOutput() {
		return output;
	}
}
