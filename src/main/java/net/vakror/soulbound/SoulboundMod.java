package net.vakror.soulbound;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;
import net.vakror.soulbound.client.SoulboundClient;
import net.vakror.soulbound.compat.hammerspace.DungeonEvents;
import net.vakror.soulbound.compat.hammerspace.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.hammerspace.blocks.entity.ModDungeonBlockEntities;
import net.vakror.soulbound.compat.hammerspace.dimension.Dimensions;
import net.vakror.soulbound.compat.hammerspace.entity.GoblaggerEntity;
import net.vakror.soulbound.compat.hammerspace.entity.ModDungeonEntities;
import net.vakror.soulbound.compat.hammerspace.items.ModDungeonItems;
import net.vakror.soulbound.compat.hammerspace.structure.util.DungeonFileLocations;
import net.vakror.soulbound.compat.hammerspace.structure.ModStructures;
import net.vakror.soulbound.compat.hammerspace.structure.piece.ModDungeonPieces;
import net.vakror.soulbound.compat.hammerspace.structure.util.DungeonSpawnPointUtils;
import net.vakror.soulbound.entity.ModEntities;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.model.WandModelLoader;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.screen.ModMenuTypes;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.soul.ModSoulTypes;
import net.vakror.soulbound.world.biome.SoulboundRegion;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import terrablender.api.Regions;

import static net.vakror.soulbound.SoulboundMod.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class SoulboundMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "soulbound";

    public static SoulboundMod instance;
    public MinecraftServer server;

    public SoulboundMod() {
        instance = this;

        DungeonFileLocations.init();
        DungeonSpawnPointUtils.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GeckoLib.initialize();
        SealRegistry.registerSeals();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModStructures.register(modEventBus);
        ModDungeonPieces.register(modEventBus);


//        if (ModList.get().isLoaded("hammerspace")) {
            ModDungeonItems.register(modEventBus);
            ModDungeonBlocks.register(modEventBus);
            ModDungeonBlockEntities.register(modEventBus);
            ModDungeonEntities.register(modEventBus);
            Dimensions.register();
//        }

        ModSoul.register(modEventBus);
        ModSoulTypes.register(modEventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.addListener(DungeonEvents.ForgeEvents::dungeonTickEvent);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModPackets.register();
            Regions.register(new SoulboundRegion(new ResourceLocation(MOD_ID, "soulbound_region"), 1));
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        SoulboundClient.doClientRegister(event);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        instance.server = event.getServer();
    }
    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent e) {
        instance.server = null;
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Dispatch IMC
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Receive and process InterModComms from other mods
    }
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModelRegistryEvents {
        @SubscribeEvent
        public static void onModelsRegistered(ModelEvent.RegisterGeometryLoaders event) {
            event.register("wand", WandModelLoader.INSTANCE);
        }
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            if (ModList.get().isLoaded("hammerspace")) {
                event.put(ModDungeonEntities.GOBLAGGER.get(), GoblaggerEntity.setAttributes());
            }
        }
//        @SubscribeEvent
//        public static void onTextureStitch(TextureStitchEvent.Pre event) {
//            if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/activated/axing"));
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/wand"));
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/activated/pickaxing"));
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/activated/scythe"));
//            }
//        }
    }
}
