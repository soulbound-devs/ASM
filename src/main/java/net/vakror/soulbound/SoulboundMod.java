package net.vakror.soulbound;

import com.mojang.logging.LogUtils;
import dev.architectury.event.EventResult;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.vakror.registry.jamesregistryapi.api.event.RegistryEvents;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.block.entity.ModBlockEntities;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.dungeon.blocks.entity.ModDungeonBlockEntities;
import net.vakror.soulbound.compat.dungeon.dimension.Dimensions;
import net.vakror.soulbound.compat.dungeon.items.ModDungeonItems;
import net.vakror.soulbound.extension.DefaultSoulboundExtension;
import net.vakror.soulbound.extension.dungeon.structure.ModStructures;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.model.wand.WandModelLoader;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.screen.*;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.soul.ModSoulTypes;
import net.vakror.soulbound.tab.ModCreativeModeTabs;
import net.vakror.soulbound.world.biome.SoulboundRegion;
import org.slf4j.Logger;
import terrablender.api.Regions;

public class SoulboundMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "soulbound";

    public static SoulboundMod instance;

    public SoulboundMod(IEventBus modEventBus) {
        instance = this;

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModStructures.REGISTER.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        if (ModList.get().isLoaded("hammerspace")) {
            ModDungeonItems.register();
            ModDungeonBlocks.register();
            ModDungeonBlockEntities.register();
            Dimensions.register();
            DungeonAttachments.register();
        }

        ModSoul.register(modEventBus);
        ModSoulTypes.register(modEventBus);

        RegistryEvents.SETUP_REGISTRY_EVENT.register((event -> {
            DefaultSoulboundExtension.registerAllExtensions();
            return EventResult.pass();
        }));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        ModPackets.register();
        Regions.register(new SoulboundRegion(new ResourceLocation(MOD_ID, "soulbound_region"), 1));
    }

    public void clientSetup(final FMLCommonSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModSoul.SOURCE_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.FLOWING_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.SOURCE_DARK_SOUL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModSoul.FLOWING_DARK_SOUL.get(), RenderType.translucent());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModelRegistryEvents {
        @SubscribeEvent
        public static void onModelsRegistered(ModelEvent.RegisterGeometryLoaders event) {
            event.register(new ResourceLocation(MOD_ID, "wand"), WandModelLoader.INSTANCE);
        }

        @SubscribeEvent
        public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
            if (event.getTab().equals(ModCreativeModeTabs.SOULBOUND_TAB.get())) {
                //todo: add all items
            }
        }

        @SubscribeEvent
        public static void creativeTab(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.WAND_IMBUING_MENU.get(), WandImbuingScreen::new);
            event.register(ModMenuTypes.SOUL_SOLIDIFIER_MENU.get(), SoulSolidifierScreen::new);
            event.register(ModMenuTypes.SACK_MENU.get(), SackScreen::new);
            event.register(ModMenuTypes.SOUL_EXTRACTOR_MENU.get(), SoulExtractorScreen::new);
        }
    }
}
