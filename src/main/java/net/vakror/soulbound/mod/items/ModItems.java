package net.vakror.soulbound.mod.items;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.items.custom.SackItem;
import net.vakror.soulbound.mod.items.custom.WandItem;
import net.vakror.soulbound.mod.items.custom.seals.SealItem;
import net.vakror.soulbound.mod.seal.SealType;
import net.vakror.soulbound.mod.seal.tier.sealable.ModWandTiers;
import net.vakror.soulbound.mod.soul.ModSoul;
import net.vakror.soulbound.mod.tab.ModCreativeModeTabs;

import static net.vakror.soulbound.mod.seal.SealTooltips.*;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS_REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoulboundMod.MOD_ID);

    public static final RegistryObject<Item> AXING_SEAL = ITEMS_REGISTRY.register("axing_seal",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "axing", SealType.OFFENSIVE, AXING));

    public static final RegistryObject<Item> SACK_ROW_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_row_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "row_tier_1", SealType.AMPLIFYING, ((tier -> tier.getTier() > 0 ? ((Math.min(tier.getTier() * 2, 1))): 1)),HEIGHT));

    public static final RegistryObject<Item> SACK_COLUMN_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_column_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "column_tier_1", SealType.AMPLIFYING, ((tier -> tier.getTier() > 0 ? ((Math.min(tier.getTier() * 2, 1))): 1)), WIDTH));

    public static final RegistryObject<Item> SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_stack_size_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "stack_size_tier_1", SealType.AMPLIFYING, ((tier -> tier.getTier() > 0 ? ((Math.min(tier.getTier() * 2, 2))): 1)), STACK_SIZE));

    public static final RegistryObject<Item> SACK_PICKUP_SEAL = ITEMS_REGISTRY.register("sack_pickup_seal",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "pickup", SealType.AMPLIFYING, PICKUP));

    public static final RegistryObject<Item> WAND = ITEMS_REGISTRY.register("wand",
           () -> new WandItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), ModWandTiers.ANCIENT_OAK));

    public static final RegistryObject<Item> PICKAXING_SEAL = ITEMS_REGISTRY.register("pickaxing_seal",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "pickaxing", SealType.PASSIVE, PICKING));

    public static final RegistryObject<Item> HOEING_SEAL = ITEMS_REGISTRY.register("hoeing_seal",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "hoeing", SealType.PASSIVE, HOEING));

    public static final RegistryObject<Item> MINING_SPEED_SEAL_TIER_1 = ITEMS_REGISTRY.register("mining_speed_seal",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "mining_speed_tier_1", SealType.AMPLIFYING, HASTE));

    public static final RegistryObject<Item> MINING_SPEED_SEAL_TIER_2 = ITEMS_REGISTRY.register("mining_speed_seal_tier_2",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "mining_speed_tier_2", SealType.AMPLIFYING, HASTE_TIER_2));

    public static final RegistryObject<Item> MINING_SPEED_SEAL_TIER_3 = ITEMS_REGISTRY.register("mining_speed_seal_tier_3",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), "mining_speed_tier_3", SealType.AMPLIFYING, HASTE_TIER_3));

    public static final RegistryObject<Item> SWORDING_SEAL = ITEMS_REGISTRY.register("swording_seal",
           () -> new SealItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB),"swording", SealType.OFFENSIVE, SWORDING));

    public static final RegistryObject<Item> SOUL = ITEMS_REGISTRY.register("soul",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> DARK_SOUL = ITEMS_REGISTRY.register("dark_soul",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> BLANK_PASSIVE_SEAL = ITEMS_REGISTRY.register("blank_passive_seal",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> BLANK_ATTACK_SEAL = ITEMS_REGISTRY.register("blank_attack_seal",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> BLANK_AMPLIFYING_SEAL = ITEMS_REGISTRY.register("blank_amplifying_seal",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> SOUL_BUCKET = ITEMS_REGISTRY.register("soul_bucket",
           () -> new BucketItem(ModSoul.SOURCE_SOUL, new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB).stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> DARK_SOUL_BUCKET = ITEMS_REGISTRY.register("dark_soul_bucket",
           () -> new BucketItem(ModSoul.SOURCE_DARK_SOUL, new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB).stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS_REGISTRY.register("tungsten_ingot",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> SACK = ITEMS_REGISTRY.register("sack",
           () -> new SackItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), ModWandTiers.SACK));

    public static final RegistryObject<Item> BLOOD_SOUL_SACK = ITEMS_REGISTRY.register("blood_soul_sack",
           () -> new SackItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), ModWandTiers.BLOOD_SOUL_SACK));

    public static final RegistryObject<Item> WARPED_SOUL_SACK = ITEMS_REGISTRY.register("warped_soul_sack",
           () -> new SackItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), ModWandTiers.WARPED_SACK));

    public static final RegistryObject<Item> PURPUR_SACK = ITEMS_REGISTRY.register("purpur_sack",
           () -> new SackItem(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB), ModWandTiers.PURPUR_SACK));

    public static final RegistryObject<Item> RAW_TUNGSTEN = ITEMS_REGISTRY.register("raw_tungsten",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));

    public static final RegistryObject<Item> CORRUPTED_BERRIES = ITEMS_REGISTRY.register("corrupted_berries",
           () -> new Item(new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB).food(ModFoodProperties.CORRUPTED_BERRY)));

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}
