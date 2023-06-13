package net.vakror.asm.util;

import org.apache.commons.lang3.Validate;

public class ItemCountRenderHandler {
	private static ItemCountRenderHandler instance = new ItemCountRenderHandler();

	public String toConsiseString(int count) {
		return String.valueOf(count);
	}

	/**
	 * How big or small to render the item count text
	 * @return 1 == vanilla
	 */
	public float scale(String string) {
		if (string != null) {
			if (string.length() > 3) {
				return .5f;
			} else if (string.length() == 3) {
				return .75f;
			}
		}
		return 1f;
	}


	public static ItemCountRenderHandler getInstance() {
		return instance;
	}

	/**
	 * mods can extend this class and set the instance.
	 * There can only really be one handler, so it overrides any others.
	 */
	public static void setInstance(ItemCountRenderHandler instance) {
		Validate.notNull(instance, "instance cannot be null");
		ItemCountRenderHandler.instance = instance;
	}
}