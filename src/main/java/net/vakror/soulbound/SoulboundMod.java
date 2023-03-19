package net.vakror.soulbound;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;
import net.vakror.soulbound.client.SoulboundClient;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.model.WandModelLoader;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.screen.ModMenuTypes;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.soul.ModSoulTypes;
import net.vakror.soulbound.world.biome.ModBiomes;
import net.vakror.soulbound.world.biome.SoulboundRegion;
import net.vakror.soulbound.world.dimension.Dimensions;
import net.vakror.soulbound.world.feature.ModConfiguredFeatures;
import net.vakror.soulbound.world.feature.ModPlacedFeatures;
import org.slf4j.Logger;
import terrablender.api.Regions;
import top.theillusivec4.curios.api.SlotTypeMessage;

import static net.vakror.soulbound.SoulboundMod.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class SoulboundMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "soulbound";

    public SoulboundMod() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SealRegistry.registerSeals();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBiomes.register(modEventBus);
        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        Dimensions.register();

        ModSoul.register(modEventBus);
        ModSoulTypes.register(modEventBus);

//        Regions.register(new SoulboundNetherRegion(new ResourceLocation(MOD_ID, "soulbound_nether_region"), 1));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
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
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Dispatch IMC
        InterModComms.sendTo("curios", "REGISTER_TYPE", () -> new SlotTypeMessage.Builder("necklace").priority(3).size(2).build());
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
        public static void onTextureStitch(TextureStitchEvent.Pre event) {
            if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
                event.addSprite(new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/axing"));
                event.addSprite(new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/wand"));
                event.addSprite(new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/pickaxing"));
                event.addSprite(new ResourceLocation(SoulboundMod.MOD_ID, "item/wands/activated/scythe"));
            }
        }
    }
}
