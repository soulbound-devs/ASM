package net.vakror.soulbound.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.soulbound.compat.hammerspace.entity.ModDungeonEntities;
import net.vakror.soulbound.compat.hammerspace.entity.client.GoblaggerRenderer;
import net.vakror.soulbound.screen.ModMenuTypes;
import net.vakror.soulbound.screen.SackScreen;
import net.vakror.soulbound.screen.SoulSolidifierScreen;
import net.vakror.soulbound.screen.WandImbuingScreen;
import net.vakror.soulbound.soul.ModSoul;

public class SoulboundClient {
    public static void doClientRegister(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.WAND_IMBUING_MENU.get(), WandImbuingScreen::new);
        MenuScreens.register(ModMenuTypes.SOUL_SOLIDIFIER_MENU.get(), SoulSolidifierScreen::new);
        MenuScreens.register(ModMenuTypes.SACK_MENU.get(), SackScreen::new);

        ItemBlockRenderTypes.setRenderLayer(ModSoul.SOURCE_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.FLOWING_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.SOURCE_DARK_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.FLOWING_DARK_SOUL.get(), RenderType.translucent());

        if (ModList.get().isLoaded("hammerspace")) {
            EntityRenderers.register(ModDungeonEntities.GOBLAGGER.get(), GoblaggerRenderer::new);
        }
    }
}
