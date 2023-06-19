package net.vakror.asm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String SWITCH_PICKUP_MODE_KEY = "key.asm.pickup_mode_switch";
    public static final String ASM_KEY_CATEGORY = "key.category.asm";

    public static final KeyMapping PICKUP_KEY = new KeyMapping(SWITCH_PICKUP_MODE_KEY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT, ASM_KEY_CATEGORY);
}