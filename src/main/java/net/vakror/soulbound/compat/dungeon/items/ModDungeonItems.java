package net.vakror.soulbound.compat.dungeon.items;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import static net.vakror.soulbound.items.ModItems.ITEMS_REGISTRY;

public class ModDungeonItems {

    @Nullable
    public static DeferredHolder<Item, Item> KEY;

    public static void register() {
        KEY = ITEMS_REGISTRY.register("key",
                () -> new Item(new Item.Properties()));
    }
}
