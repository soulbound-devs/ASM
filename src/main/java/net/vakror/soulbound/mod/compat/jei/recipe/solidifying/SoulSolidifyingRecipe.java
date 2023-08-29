package net.vakror.soulbound.mod.compat.jei.recipe.solidifying;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.mod.items.ModItems;
import net.vakror.soulbound.mod.soul.SoulType;
import org.jetbrains.annotations.Unmodifiable;

public class SoulSolidifyingRecipe implements ISoulSolidifyingRecipe {

	private final SoulType soulType;
	private final Item ingotItem;
	private final ItemStack output;

	public static ISoulSolidifyingRecipe create(Item ingot, SoulType soulType) {
		SoulSolidifyingRecipe recipe = new SoulSolidifyingRecipe(soulType, ingot, new ItemStack(soulType.equals(SoulType.NORMAL) ? ModItems.SOUL.get(): ModItems.DARK_SOUL.get()));

		return recipe;
	}

	public SoulSolidifyingRecipe(SoulType soulType, Item ingotItem, ItemStack output) {
		this.soulType = soulType;
		this.ingotItem = ingotItem;
		this.output = output;
	}


	@Override
	public @Unmodifiable SoulType getSoulType() {
		return soulType;
	}

	@Override
	public @Unmodifiable Item getIngotItem() {
		return ingotItem;
	}

	@Override
	public @Unmodifiable ItemStack getOutput() {
		return output;
	}
}
