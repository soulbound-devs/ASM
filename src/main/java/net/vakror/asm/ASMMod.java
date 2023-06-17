package net.vakror.asm;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.blocks.entity.ModBlockEntities;
import net.vakror.asm.client.ASMClient;
import net.vakror.asm.entity.ModEntities;
import net.vakror.asm.items.ModItems;
import net.vakror.asm.model.WandModelLoader;
import net.vakror.asm.packets.ModPackets;
import net.vakror.asm.screen.ModMenuTypes;
import net.vakror.asm.seal.SealRegistry;
import net.vakror.asm.soul.ModSoul;
import net.vakror.asm.soul.ModSoulTypes;
import net.vakror.asm.tab.ModCreativeModeTabs;
import net.vakror.asm.world.biome.ASMRegion;
import net.vakror.asm.world.dimension.Dimensions;
import net.vakror.asm.world.structure.ModDungeonPieces;
import net.vakror.asm.world.structure.ModStructures;
import org.slf4j.Logger;
import terrablender.api.Regions;

import static net.vakror.asm.ASMMod.MOD_ID;
import static net.vakror.asm.blocks.ModBlocks.*;
import static net.vakror.asm.items.ModItems.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class ASMMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "asm";

    public static ASMMod instance;
    public MinecraftServer server;

    public ASMMod() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildContents);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeModeTabs.register(modEventBus);
        SealRegistry.registerSeals();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModStructures.register(modEventBus);
        ModDungeonPieces.register(modEventBus);
        ModEntities.register(modEventBus);

        Dimensions.register();

        ModSoul.register(modEventBus);
        ModSoulTypes.register(modEventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModPackets.register();
            Regions.register(new ASMRegion(new ResourceLocation(MOD_ID, "asm_region"), 1));
        });
    }

    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to ingredients tab
        if (event.getTabKey() == ModCreativeModeTabs.ASM_TAB.getKey()) {
            event.accept(SACK.get());
            event.accept(WAND.get());
            event.accept(AXING_SEAL.get());
            event.accept(PICKAXING_SEAL.get());
            event.accept(HOEING_SEAL.get());
            event.accept(MINING_SPEED_SEAL_TIER_1.get());
            event.accept(MINING_SPEED_SEAL_TIER_2.get());
            event.accept(MINING_SPEED_SEAL_TIER_3.get());
            event.accept(SWORDING_SEAL.get());
            event.accept(SACK_ROW_UPGRADE_SEAL_TIER_1.get());
            event.accept(SACK_COLUMN_UPGRADE_SEAL_TIER_1.get());
            event.accept(SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1.get());
            event.accept(SOUL.get());
            event.accept(DARK_SOUL.get());
            event.accept(BLANK_PASSIVE_SEAL.get());
            event.accept(BLANK_ATTACK_SEAL.get());
            event.accept(BLANK_AMPLIFYING_SEAL.get());
            event.accept(SOUL_BUCKET.get());
            event.accept(DARK_SOUL_BUCKET.get());
            event.accept(TUNGSTEN_ORE.get());
            event.accept(RAW_TUNGSTEN.get());
            event.accept(TUNGSTEN_INGOT.get());
            event.accept(TUNGSTEN_BLOCK.get());
            event.accept(KEY.get());
            event.accept(SACK.get());
            event.accept(WAND_IMBUING_TABLE.get());
            event.accept(SOUL_CATALYST.get());
            event.accept(SOUL_SOLIDIFIER.get());
            event.accept(ANCIENT_OAK_LOG.get());
            event.accept(ANCIENT_OAK_PLANKS.get());
            event.accept(CORRUPTED_LOG.get());
            event.accept(CORRUPTED_LEAVES.get());
            event.accept(CORRUPTED_PLANKS.get());
            event.accept(CORRUPTED_BERRIES.get());
            event.accept(DUNGEON_KEY_BLOCK.get());
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ASMClient.doClientRegister(event);
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
//        @SubscribeEvent
//        public static void onTextureStitch(TextureStitchEvent. event) {
//            if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/activated/axing"));
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/wand"));
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/activated/pickaxing"));
//                event.addSprite(new ResourceLocation(ASMMod.MOD_ID, "item/wands/activated/scythe"));
//            }
//        }
    }
}
