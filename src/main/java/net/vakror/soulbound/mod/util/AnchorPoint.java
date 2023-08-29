package net.vakror.soulbound.mod.util;

import net.minecraft.client.gui.screens.TitleScreen;

import java.util.function.Function;

public enum AnchorPoint {
	TOP_LEFT(s -> 0, s -> 0),
	TOP_CENTER(s -> s.width / 2, s -> 0),
	TOP_RIGHT(s -> s.width, s -> 0),
	MIDDLE_LEFT(s -> 0, s -> s.height / 2),
	MIDDLE_CENTER(s -> s.width / 2, s -> s.height / 2),
	MIDDLE_RIGHT(s -> s.width, s -> s.height / 2),
	BOTTOM_LEFT(s -> 0, s -> s.height),
	BOTTOM_CENTER(s -> s.width / 2, s -> s.height),
	BOTTOM_RIGHT(s -> s.width, s -> s.height),
	DEFAULT(s -> s.width / 2, s -> s.height / 4 + 48),
	DEFAULT_LOGO(s -> s.width / 2, s -> s.height / 4),
	SPLASH(s -> s.width / 2 + 90, s -> 70),
	TITLE(s -> s.width / 2 - 137, s -> 30),
	JAVAED(s -> s.width / 2 - 137 + 88, s -> 67),
	FORGE(s -> 0, s -> 0);

	private final Function<TitleScreen, Integer> xFunc;
	private final Function<TitleScreen, Integer> yFunc;

	private AnchorPoint(Function<TitleScreen, Integer> xFunc, Function<TitleScreen, Integer> yFunc) {
		this.xFunc = xFunc;
		this.yFunc = yFunc;
	}

	public int getX(TitleScreen scn) {
		return this.xFunc.apply(scn);
	}

	public int getY(TitleScreen scn) {
		return this.yFunc.apply(scn);
	}
}