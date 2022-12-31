package net.vakror.soulbound;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;
import net.vakror.soulbound.client.SoulBoundClient;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.networking.ModPackets;
import net.vakror.soulbound.screen.ModMenuTypes;
import net.vakror.soulbound.seal.SealRegistry;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

import static net.vakror.soulbound.SoulboundMod.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class SoulboundMod {
    private static final Logger LOGGER = LogUtils.getLogger();
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

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModPackets::register);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        SoulBoundClient.doClientRegister(event);
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

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
