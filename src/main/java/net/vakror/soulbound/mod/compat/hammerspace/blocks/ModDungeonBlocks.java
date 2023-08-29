package net.vakror.soulbound.mod.compat.hammerspace.blocks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.hammerspace.blocks.custom.DungeonAccessBlock;
import net.vakror.soulbound.mod.compat.hammerspace.blocks.custom.ReturnToOverworldBlock;
import net.vakror.soulbound.mod.items.ModItems;
import net.vakror.soulbound.mod.tab.ModCreativeModeTabs;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;


public class ModDungeonBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SoulboundMod.MOD_ID);

    public static final RegistryObject<Block> RETURN_TO_OVERWORLD_BLOCK = registerBlock("return_to_overworld_block",
            () -> new ReturnToOverworldBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(3.0F, 3.0F).noLootTable()));

    public static final RegistryObject<Block> DUNGEON_BORDER = registerBlock("dungeon_border",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(-1.0F, 3600000.0F).noLootTable()));

    public static final RegistryObject<Block> DUNGEON_KEY_BLOCK = registerBlock("dungeon_key_block",
            () -> new DungeonAccessBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F, 6.0F).requiresCorrectToolForDrops()));

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, String tooltipKey) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tooltipKey);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, String tooltipKey) {
        return ModItems.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties()){
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(Component.translatable(tooltipKey));
            }
        });
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(ModCreativeModeTabs.SOULBOUND_TAB)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
