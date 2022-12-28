package net.vakror.soulbound.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulBoundMod;
import net.vakror.soulbound.items.custom.WandItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoulBoundMod.MOD_ID);

    public static final RegistryObject<Item> AXING_SEAL = ITEMS.register("axing_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> WAND = ITEMS.register("wand",
            () -> new WandItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> PICKAXING_SEAL = ITEMS.register("pickaxing_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> HOEING_SEAL = ITEMS.register("hoeing_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
