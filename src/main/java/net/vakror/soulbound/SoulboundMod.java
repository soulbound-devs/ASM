package net.vakror.soulbound;

import com.mojang.logging.LogUtils;
import dev.architectury.event.EventResult;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.vakror.registry.jamesregistryapi.api.event.RegistryEvents;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.block.block.SoulExtractorBlock;
import net.vakror.soulbound.block.entity.ModBlockEntities;
import net.vakror.soulbound.block.entity.custom.SoulSolidifierBlockEntity;
import net.vakror.soulbound.block.entity.custom.WandImbuingTableBlockEntity;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.dungeon.blocks.entity.ModDungeonBlockEntities;
import net.vakror.soulbound.compat.dungeon.dimension.Dimensions;
import net.vakror.soulbound.compat.dungeon.items.ModDungeonItems;
import net.vakror.soulbound.entity.ModEntities;
import net.vakror.soulbound.extension.DefaultSoulboundExtension;
import net.vakror.soulbound.extension.dungeon.structure.ModStructures;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.model.wand.api.WandModelLoader;
import net.vakror.soulbound.packets.SoulFluidSyncS2CPacket;
import net.vakror.soulbound.packets.SyncPickupModeC2SPacket;
import net.vakror.soulbound.packets.SyncSoulS2CPacket;
import net.vakror.soulbound.screen.*;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.soul.ModSoulTypes;
import net.vakror.soulbound.tab.ModCreativeModeTabs;
import net.vakror.soulbound.world.biome.SoulboundRegion;
import org.slf4j.Logger;
import terrablender.api.Regions;

@Mod(SoulboundMod.MOD_ID)
public class SoulboundMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "soulbound";

    public static SoulboundMod instance;
    public MinecraftServer server;

    public SoulboundMod(IEventBus modEventBus) {
        instance = this;

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::serverSetup);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModStructures.REGISTER.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModDungeonItems.register();
        ModDungeonBlocks.register();
        ModDungeonBlockEntities.register();
        Dimensions.register();
        DungeonAttachments.register();

        ModSoul.register(modEventBus);
        ModSoulTypes.register(modEventBus);

        RegistryEvents.SETUP_REGISTRY_EVENT.register((event -> {
            DefaultSoulboundExtension.registerAllExtensions();
            return EventResult.pass();
        }));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Regions.register(new SoulboundRegion(new ResourceLocation(MOD_ID, "soulbound_region"), 1));
    }

    private void serverSetup(FMLDedicatedServerSetupEvent event) {
        instance.server = server;
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
                RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY).registryOrThrow(Registries.ITEM).entrySet().forEach((entry) -> {
                    if (entry.getKey().location().getNamespace().equals(SoulboundMod.MOD_ID)) {
                        event.accept(entry.getValue());
                    }
                });
            }
        }

        @SubscribeEvent
        public static void registerMenus(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.WAND_IMBUING_MENU.get(), WandImbuingScreen::new);
            event.register(ModMenuTypes.SOUL_SOLIDIFIER_MENU.get(), SoulSolidifierScreen::new);
            event.register(ModMenuTypes.SACK_MENU.get(), SackScreen::new);
            event.register(ModMenuTypes.SOUL_EXTRACTOR_MENU.get(), SoulExtractorScreen::new);
        }

        @SubscribeEvent
        public static void register(final RegisterPayloadHandlerEvent event) {
            final IPayloadRegistrar registrar = event.registrar(SoulboundMod.MOD_ID);

            registrar.play(SoulFluidSyncS2CPacket.ID, SoulFluidSyncS2CPacket::new, SoulFluidSyncS2CPacket::handle);
            registrar.play(SyncSoulS2CPacket.ID, SyncSoulS2CPacket::new, SyncSoulS2CPacket::handle);

            registrar.play(SyncPickupModeC2SPacket.ID, SyncPickupModeC2SPacket::new, SyncPickupModeC2SPacket::handle);
        }

        @SubscribeEvent
        public static void registerBlockCaps(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.SOUL_EXTRACTOR_BLOCK_ENTITY.get(), (entity, direction) -> {
                Direction localDir = entity.getBlockState().getValue(SoulExtractorBlock.FACING);
                if (direction != null) {
                    return switch (localDir) {
                        default -> direction.getOpposite() == Direction.EAST ? entity.SOUL_TANK : direction.getOpposite() == Direction.WEST ? entity.DARK_SOUL_TANK : null;
                        case EAST -> direction.getClockWise() == Direction.EAST ? entity.SOUL_TANK : direction.getClockWise() == Direction.WEST ? entity.DARK_SOUL_TANK : null;
                        case SOUTH -> direction == Direction.EAST ? entity.SOUL_TANK : direction == Direction.WEST ? entity.DARK_SOUL_TANK: null;
                        case WEST -> direction.getCounterClockWise() == Direction.EAST ? entity.SOUL_TANK : direction.getCounterClockWise() == Direction.WEST ? entity.DARK_SOUL_TANK : null;
                    };
                }
                return null;
            });

            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntities.SOUL_SOLIDIFIER_BLOCK_ENTITY.get(), SoulSolidifierBlockEntity::getFluidTank);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.SOUL_SOLIDIFIER_BLOCK_ENTITY.get(), SoulSolidifierBlockEntity::getItemHandler);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.WAND_IMBUING_TABLE_BLOCK_ENTITY.get(), WandImbuingTableBlockEntity::getItemHandler);
        }
    }
}
