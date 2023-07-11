package net.vakror.soulbound.compat.jei.recipe.extracting;

import net.minecraftforge.fluids.FluidStack;
import net.vakror.soulbound.soul.ModSoul;
import org.jetbrains.annotations.Unmodifiable;

public class SoulExtractingRecipe implements ISoulExtractingRecipe {

	private final int playerSoulAmount;
	private final int playerDarkSoulAmount;
	private final FluidStack soulFluid;
	private final FluidStack darkSoulFluid;

	public static ISoulExtractingRecipe create() {
		return new SoulExtractingRecipe(100, 100, new FluidStack(ModSoul.SOURCE_SOUL.get(), 1000), new FluidStack(ModSoul.SOURCE_DARK_SOUL.get(), 1000));
	}

	public SoulExtractingRecipe(int playerSoulAmount, int playerDarkSoulAmount, FluidStack soulFluid, FluidStack darkSoulFluid) {
		this.playerSoulAmount = playerSoulAmount;
		this.playerDarkSoulAmount = playerDarkSoulAmount;
		this.soulFluid = soulFluid;
		this.darkSoulFluid = darkSoulFluid;
	}

	@Override
	public @Unmodifiable int getPlayerSoulAmount() {
		return playerSoulAmount;
	}

	@Override
	public @Unmodifiable int getPlayerDarkSoulAmount() {
		return playerDarkSoulAmount;
	}

	@Override
	public @Unmodifiable FluidStack getSoulFluid() {
		return soulFluid;
	}

	@Override
	public @Unmodifiable FluidStack getDarkSoulFluid() {
		return darkSoulFluid;
	}
}
