package net.vakror.soulbound.compat.hammerspace.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.tab.ModCreativeModeTabs;

public class ModDungeonItems {
    public static final DeferredRegister<Item> ITEMS_REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoulboundMod.MOD_ID);

    public static final RegistryObject<Item> KEY = ITEMS_REGISTRY.register("key",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}
