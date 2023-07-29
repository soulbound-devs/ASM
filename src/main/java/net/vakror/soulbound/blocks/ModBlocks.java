package net.vakror.soulbound.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.custom.ModFlammableRotatedPillarBlock;
import net.vakror.soulbound.blocks.custom.SoulCatalystBlock;
import net.vakror.soulbound.blocks.custom.SoulSolidifierBlock;
import net.vakror.soulbound.blocks.custom.WandImbuingTableBlock;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.tab.ModCreativeModeTabs;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SoulboundMod.MOD_ID);

    public static final RegistryObject<Block> WAND_IMBUING_TABLE = registerBlock("wand_imbuing_table",
           () -> new WandImbuingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> SOUL_CATALYST = registerBlock("soul_catalyst",
           () -> new SoulCatalystBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> SOUL_SOLIDIFIER = registerBlock("soul_solidifier",
           () -> new SoulSolidifierBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(5).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<Block> ANCIENT_OAK_LOG = registerBlock("ancient_oak_log",
           () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> SOUL_EXTRACTOR = registerBlock("soul_extractor",
           () -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(5).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ANCIENT_OAK_PLANKS = registerBlock("ancient_oak_planks",
           () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final RegistryObject<Block> CORRUPTED_LOG = registerBlock("corrupted_log",
           () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> CORRUPTED_LEAVES = registerBlock("corrupted_leaves",
           () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> CORRUPTED_PLANKS = registerBlock("corrupted_planks",
           () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final RegistryObject<LiquidBlock> SOUL_FLUID_BLOCK = registerBlockWithoutBlockItem("soul_fluid_block",
           () -> new LiquidBlock(ModSoul.SOURCE_SOUL, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<LiquidBlock> DARK_SOUL_FLUID_BLOCK = registerBlockWithoutBlockItem("dark_soul_fluid_block",
           () -> new LiquidBlock(ModSoul.SOURCE_DARK_SOUL, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> TUNGSTEN_ORE = registerBlock("tungsten_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(3, 6)));

    public static final RegistryObject<Block> TUNGSTEN_BLOCK = registerBlock("tungsten_block",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(3, 6)));

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
