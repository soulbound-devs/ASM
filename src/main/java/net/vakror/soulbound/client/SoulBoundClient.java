package net.vakror.soulbound.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.soulbound.screen.ModMenuTypes;
import net.vakror.soulbound.screen.WandImbuingScreen;

public class SoulBoundClient {
    public static void doClientRegister(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.WAND_IMBUING_MENU.get(), WandImbuingScreen::new);
    }
}
