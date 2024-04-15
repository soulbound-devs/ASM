package net.vakror.soulbound.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.SackItem;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.seal.SealType;
import net.vakror.soulbound.seal.tier.sealable.ModWandTiers;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.tab.ModCreativeModeTabs;

import static net.vakror.soulbound.seal.SealTooltips.*;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS_REGISTRY =
            DeferredRegister.create(Registries.ITEM, SoulboundMod.MOD_ID);

    public static final DeferredHolder<Item, SealItem> AXING_SEAL = ITEMS_REGISTRY.register("axing_seal",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "axing"), SealType.OFFENSIVE, AXING));

    public static final DeferredHolder<Item, SealItem> SACK_ROW_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_row_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "row_tier_1"), SealType.AMPLIFYING, ((tier -> tier.getTier() > 0 ? ((Math.min(tier.getTier() * 2, 1))): 1)),HEIGHT));

    public static final DeferredHolder<Item, SealItem> SACK_COLUMN_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_column_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "column_tier_1"), SealType.AMPLIFYING, ((tier -> tier.getTier() > 0 ? ((Math.min(tier.getTier() * 2, 1))): 1)), WIDTH));

    public static final DeferredHolder<Item, SealItem> SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1 = ITEMS_REGISTRY.register("sack_stack_size_upgrade_seal_tier_1",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "stack_size_tier_1"), SealType.AMPLIFYING, ((tier -> tier.getTier() > 0 ? ((Math.min(tier.getTier() * 2, 2))): 1)), STACK_SIZE));

    public static final DeferredHolder<Item, SealItem> SACK_PICKUP_SEAL = ITEMS_REGISTRY.register("sack_pickup_seal",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "pickup"), SealType.AMPLIFYING, PICKUP));

    public static final DeferredHolder<Item, WandItem> WAND = ITEMS_REGISTRY.register("wand",
           () -> new WandItem(new Item.Properties(), ModWandTiers.ANCIENT_OAK));

    public static final DeferredHolder<Item, SealItem> PICKAXING_SEAL = ITEMS_REGISTRY.register("pickaxing_seal",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "pickaxing"), SealType.PASSIVE, PICKING));

    public static final DeferredHolder<Item, SealItem> HOEING_SEAL = ITEMS_REGISTRY.register("hoeing_seal",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "hoeing"), SealType.PASSIVE, HOEING));

    public static final DeferredHolder<Item, SealItem> MINING_SPEED_SEAL_TIER_1 = ITEMS_REGISTRY.register("mining_speed_seal",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "mining_speed_tier_1"), SealType.AMPLIFYING, HASTE));

    public static final DeferredHolder<Item, SealItem> MINING_SPEED_SEAL_TIER_2 = ITEMS_REGISTRY.register("mining_speed_seal_tier_2",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "mining_speed_tier_2"), SealType.AMPLIFYING, HASTE_TIER_2));

    public static final DeferredHolder<Item, SealItem> MINING_SPEED_SEAL_TIER_3 = ITEMS_REGISTRY.register("mining_speed_seal_tier_3",
           () -> new SealItem(new Item.Properties(), new ResourceLocation(SoulboundMod.MOD_ID, "mining_speed_tier_3"), SealType.AMPLIFYING, HASTE_TIER_3));

    public static final DeferredHolder<Item, SealItem> SWORDING_SEAL = ITEMS_REGISTRY.register("swording_seal",
           () -> new SealItem(new Item.Properties(),new ResourceLocation(SoulboundMod.MOD_ID, "swording"), SealType.OFFENSIVE, SWORDING));

    public static final DeferredHolder<Item, Item> SOUL = ITEMS_REGISTRY.register("soul",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> DARK_SOUL = ITEMS_REGISTRY.register("dark_soul",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BLANK_PASSIVE_SEAL = ITEMS_REGISTRY.register("blank_passive_seal",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BLANK_ATTACK_SEAL = ITEMS_REGISTRY.register("blank_attack_seal",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BLANK_AMPLIFYING_SEAL = ITEMS_REGISTRY.register("blank_amplifying_seal",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> TUNGSTEN_INGOT = ITEMS_REGISTRY.register("tungsten_ingot",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, SackItem> SACK = ITEMS_REGISTRY.register("sack",
           () -> new SackItem(new Item.Properties(), ModWandTiers.SACK));

    public static final DeferredHolder<Item, SackItem> BLOOD_SOUL_SACK = ITEMS_REGISTRY.register("blood_soul_sack",
           () -> new SackItem(new Item.Properties(), ModWandTiers.BLOOD_SOUL_SACK));

    public static final DeferredHolder<Item, SackItem> WARPED_SOUL_SACK = ITEMS_REGISTRY.register("warped_soul_sack",
           () -> new SackItem(new Item.Properties(), ModWandTiers.WARPED_SACK));

    public static final DeferredHolder<Item, SackItem> PURPUR_SACK = ITEMS_REGISTRY.register("purpur_sack",
           () -> new SackItem(new Item.Properties(), ModWandTiers.PURPUR_SACK));

    public static final DeferredHolder<Item, Item> RAW_TUNGSTEN = ITEMS_REGISTRY.register("raw_tungsten",
           () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> CORRUPTED_BERRIES = ITEMS_REGISTRY.register("corrupted_berries",
           () -> new Item(new Item.Properties().food(ModFoodProperties.CORRUPTED_BERRY)));

    public static final DeferredHolder<Item, BucketItem> SOUL_BUCKET = ITEMS_REGISTRY.register("soul_bucket",
            () -> new BucketItem(ModSoul.SOURCE_SOUL, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredHolder<Item, BucketItem> DARK_SOUL_BUCKET = ITEMS_REGISTRY.register("dark_soul_bucket",
            () -> new BucketItem(ModSoul.SOURCE_DARK_SOUL, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}
