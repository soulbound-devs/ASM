package net.vakror.soulbound.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.seal.seals.Seal;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.wand.WandTiers;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoulboundMod.MOD_ID);

    public static final RegistryObject<Item> AXING_SEAL = ITEMS.register("axing_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND), "axing", 1));

    public static final RegistryObject<Item> WAND = ITEMS.register("wand",
            () -> new WandItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND), WandTiers.ANCIENT_OAK));

    public static final RegistryObject<Item> PICKAXING_SEAL = ITEMS.register("pickaxing_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND), "pickaxing", 0));

    public static final RegistryObject<Item> HOEING_SEAL = ITEMS.register("hoeing_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND), "hoeing", 0));

    public static final RegistryObject<Item> MINING_SPEED_SEAL = ITEMS.register("mining_speed_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND), "mining_speed", 2));

    public static final RegistryObject<Item> SWORDING_SEAL = ITEMS.register("swording_seal",
            () -> new SealItem(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND), "swording", 1));

    public static final RegistryObject<Item> SOUL = ITEMS.register("soul",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> DARK_SOUL = ITEMS.register("dark_soul",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> BLANK_PASSIVE_SEAL = ITEMS.register("blank_passive_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> BLANK_ATTACK_SEAL = ITEMS.register("blank_attack_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> BLANK_AMPLIFYING_SEAL = ITEMS.register("blank_amplifying_seal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SOULBOUND)));

    public static final RegistryObject<Item> SOUL_BUCKET = ITEMS.register("soul_bucket",
            () -> new BucketItem(ModSoul.SOURCE_SOUL, new Item.Properties().tab(ModCreativeModeTab.SOULBOUND).stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> DARK_SOUL_BUCKET = ITEMS.register("dark_soul_bucket",
            () -> new BucketItem(ModSoul.SOURCE_DARK_SOUL, new Item.Properties().tab(ModCreativeModeTab.SOULBOUND).stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot",
            () -> new BucketItem(ModSoul.SOURCE_DARK_SOUL, new Item.Properties().tab(ModCreativeModeTab.SOULBOUND).stacksTo(1).craftRemainder(Items.BUCKET)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
