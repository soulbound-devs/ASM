package net.vakror.asm.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;
import net.vakror.asm.seal.tier.ModWandTiers;
import net.vakror.asm.items.custom.SackItem;
import net.vakror.asm.items.custom.WandItem;
import net.vakror.asm.items.custom.seals.SealItem;
import net.vakror.asm.seal.SealType;
import net.vakror.asm.soul.ModSoul;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS_REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, ASMMod.MOD_ID);

    public static final RegistryObject<Item> AXING_SEAL = ITEMS_REGISTRY.register("axing_seal",
           () -> new SealItem(new Item.Properties(), "axing", SealType.OFFENSIVE));

    public static final RegistryObject<Item> SACK_ROW_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_row_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties(), "row_tier_1", SealType.AMPLIFYING, true));

    public static final RegistryObject<Item> SACK_COLUMN_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_column_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties(), "column_tier_1", SealType.AMPLIFYING, true));

    public static final RegistryObject<Item> SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_stack_size_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties(), "stack_size_tier_1", SealType.AMPLIFYING, true));

    public static final RegistryObject<Item> WAND = ITEMS_REGISTRY.register("wand",
           () -> new WandItem(new Item.Properties(), ModWandTiers.ANCIENT_OAK));

    public static final RegistryObject<Item> PICKAXING_SEAL = ITEMS_REGISTRY.register("pickaxing_seal",
           () -> new SealItem(new Item.Properties(), "pickaxing", SealType.PASSIVE));

    public static final RegistryObject<Item> HOEING_SEAL = ITEMS_REGISTRY.register("hoeing_seal",
           () -> new SealItem(new Item.Properties(), "hoeing", SealType.PASSIVE));

    public static final RegistryObject<Item> MINING_SPEED_SEAL = ITEMS_REGISTRY.register("mining_speed_seal",
           () -> new SealItem(new Item.Properties(), "mining_speed", SealType.AMPLIFYING));

    public static final RegistryObject<Item> SWORDING_SEAL = ITEMS_REGISTRY.register("swording_seal",
           () -> new SealItem(new Item.Properties(),"swording", SealType.OFFENSIVE));

    public static final RegistryObject<Item> SOUL = ITEMS_REGISTRY.register("soul",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DARK_SOUL = ITEMS_REGISTRY.register("dark_soul",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLANK_PASSIVE_SEAL = ITEMS_REGISTRY.register("blank_passive_seal",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLANK_ATTACK_SEAL = ITEMS_REGISTRY.register("blank_attack_seal",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLANK_AMPLIFYING_SEAL = ITEMS_REGISTRY.register("blank_amplifying_seal",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SOUL_BUCKET = ITEMS_REGISTRY.register("soul_bucket",
           () -> new BucketItem(ModSoul.SOURCE_SOUL, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> DARK_SOUL_BUCKET = ITEMS_REGISTRY.register("dark_soul_bucket",
           () -> new BucketItem(ModSoul.SOURCE_DARK_SOUL, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS_REGISTRY.register("tungsten_ingot",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KEY = ITEMS_REGISTRY.register("key",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SACK = ITEMS_REGISTRY.register("sack",
           () -> new SackItem(new Item.Properties(), ModWandTiers.SACK));

    public static final RegistryObject<Item> RAW_TUNGSTEN = ITEMS_REGISTRY.register("raw_tungsten",
           () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CORRUPTED_BERRIES = ITEMS_REGISTRY.register("corrupted_berries",
           () -> new Item(new Item.Properties().food(ModFoodProperties.CORRUPTED_BERRY)));

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}
