package net.vakror.soulbound.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.block.SoulCatalystBlock;
import net.vakror.soulbound.block.block.SoulExtractorBlock;
import net.vakror.soulbound.block.block.SoulSolidifierBlock;
import net.vakror.soulbound.block.block.WandImbuingTableBlock;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.soul.ModSoul;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, SoulboundMod.MOD_ID);

    public static final DeferredHolder<Block, LiquidBlock> SOUL_FLUID_BLOCK = registerBlockWithoutBlockItem("soul_fluid_block",
            () -> new LiquidBlock(ModSoul.SOURCE_SOUL, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final DeferredHolder<Block, LiquidBlock> DARK_SOUL_FLUID_BLOCK = registerBlockWithoutBlockItem("dark_soul_fluid_block",
            () -> new LiquidBlock(ModSoul.SOURCE_DARK_SOUL, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final DeferredHolder<Block, WandImbuingTableBlock> WAND_IMBUING_TABLE = registerBlock("wand_imbuing_table",
            () -> new WandImbuingTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredHolder<Block, SoulCatalystBlock> SOUL_CATALYST = registerBlock("soul_catalyst",
            () -> new SoulCatalystBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredHolder<Block, SoulSolidifierBlock> SOUL_SOLIDIFIER = registerBlock("soul_solidifier",
            () -> new SoulSolidifierBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredHolder<Block, SoulExtractorBlock> SOUL_EXTRACTOR = registerBlock("soul_extractor",
            () -> new SoulExtractorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredHolder<Block, RotatedPillarBlock> ANCIENT_OAK_LOG = registerBlock("ancient_oak_log",
           () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).ignitedByLava()));

    public static final DeferredHolder<Block, Block> ANCIENT_OAK_PLANKS = registerBlock("ancient_oak_planks",
           () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).ignitedByLava()));

    public static final DeferredHolder<Block, RotatedPillarBlock> CORRUPTED_LOG = registerBlock("corrupted_log",
           () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).ignitedByLava()));

    public static final DeferredHolder<Block, RotatedPillarBlock> CORRUPTED_LEAVES = registerBlock("corrupted_leaves",
           () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).ignitedByLava()));

    public static final DeferredHolder<Block, Block> CORRUPTED_PLANKS = registerBlock("corrupted_planks",
           () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).ignitedByLava()));

    public static final DeferredHolder<Block, DropExperienceBlock> TUNGSTEN_ORE = registerBlock("tungsten_ore",
            () -> new DropExperienceBlock(UniformInt.of(3, 6), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> TUNGSTEN_BLOCK = registerBlock("tungsten_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));

    public static <T extends Block> DeferredHolder<Block, T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block, String tooltipKey) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tooltipKey);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredHolder<Block, T> block, String tooltipKey) {
        return ModItems.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties()){
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(Component.translatable(tooltipKey));
            }
        });
    }

    public static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredHolder<Block, T> block) {
        return ModItems.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
