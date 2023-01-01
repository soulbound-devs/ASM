package net.vakror.soulbound.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.screen.ModMenuTypes;
import net.vakror.soulbound.screen.WandImbuingScreen;

public class SoulboundClient {
    public static void doClientRegister(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.WAND_IMBUING_MENU.get(), WandImbuingScreen::new);

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WAND_IMBUING_TABLE.get(), RenderType.cutout());
    }
}
