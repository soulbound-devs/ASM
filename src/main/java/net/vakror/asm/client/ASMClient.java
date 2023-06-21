package net.vakror.asm.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.vakror.asm.entity.ModEntities;
import net.vakror.asm.entity.client.GoblaggerRenderer;
import net.vakror.asm.screen.ModMenuTypes;
import net.vakror.asm.screen.SackScreen;
import net.vakror.asm.screen.SoulSolidifierScreen;
import net.vakror.asm.screen.WandImbuingScreen;
import net.vakror.asm.soul.ModSoul;

public class ASMClient {
    public static void doClientRegister(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.WAND_IMBUING_MENU.get(), WandImbuingScreen::new);
        MenuScreens.register(ModMenuTypes.SOUL_SOLIDIFIER_MENU.get(), SoulSolidifierScreen::new);
        MenuScreens.register(ModMenuTypes.SACK_MENU.get(), SackScreen::new);

        ItemBlockRenderTypes.setRenderLayer(ModSoul.SOURCE_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.FLOWING_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.SOURCE_DARK_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.FLOWING_DARK_SOUL.get(), RenderType.translucent());


        EntityRenderers.register(ModEntities.GOBLAGGER.get(), GoblaggerRenderer::new);
    }
}
