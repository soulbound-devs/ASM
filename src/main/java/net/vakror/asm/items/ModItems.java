package net.vakror.asm.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;
import net.vakror.asm.items.custom.WandItem;
import net.vakror.asm.items.custom.seals.SealItem;
import net.vakror.asm.soul.ModSoul;
import net.vakror.asm.wand.WandTiers;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ASMMod.MOD_ID);

    public static final RegistryObject<Item> AXING_SEAL = ITEMS.register("axing_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.ASM), "axing", 1));

    public static final RegistryObject<Item> WAND = ITEMS.register("wand",
            () -> new WandItem(new Item.Properties().tab(ModCreativeModeTab.ASM), WandTiers.ANCIENT_OAK));

    public static final RegistryObject<Item> PICKAXING_SEAL = ITEMS.register("pickaxing_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.ASM), "pickaxing", 0));

    public static final RegistryObject<Item> HOEING_SEAL = ITEMS.register("hoeing_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.ASM), "hoeing", 0));

    public static final RegistryObject<Item> MINING_SPEED_SEAL = ITEMS.register("mining_speed_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.ASM), "mining_speed", 2));

    public static final RegistryObject<Item> SWORDING_SEAL = ITEMS.register("swording_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.ASM), "swording", 1));

    public static final RegistryObject<Item> SOUL = ITEMS.register("soul",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));

    public static final RegistryObject<Item> DARK_SOUL = ITEMS.register("dark_soul",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));

    public static final RegistryObject<Item> BLANK_PASSIVE_SEAL = ITEMS.register("blank_passive_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));

    public static final RegistryObject<Item> BLANK_ATTACK_SEAL = ITEMS.register("blank_attack_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));

    public static final RegistryObject<Item> BLANK_AMPLIFYING_SEAL = ITEMS.register("blank_amplifying_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));

    public static final RegistryObject<Item> SOUL_BUCKET = ITEMS.register("soul_bucket",
            () -> new BucketItem(ModSoul.SOURCE_SOUL, new Item.Properties().tab(ModCreativeModeTab.ASM).stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> DARK_SOUL_BUCKET = ITEMS.register("dark_soul_bucket",
            () -> new BucketItem(ModSoul.SOURCE_DARK_SOUL, new Item.Properties().tab(ModCreativeModeTab.ASM).stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));

    public static final RegistryObject<Item> KEY = ITEMS.register("key",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.ASM)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
