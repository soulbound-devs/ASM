package net.vakror.soulbound.compat.dungeon.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.compat.dungeon.blocks.custom.DungeonAccessBlock;
import net.vakror.soulbound.compat.dungeon.blocks.custom.ReturnToOverworldBlock;
import org.jetbrains.annotations.Nullable;

import static net.vakror.soulbound.block.ModBlocks.registerBlock;


public class ModDungeonBlocks {

    @Nullable
    public static DeferredHolder<Block, ReturnToOverworldBlock> RETURN_TO_OVERWORLD_BLOCK;

    @Nullable
    public static DeferredHolder<Block, Block> DUNGEON_BORDER;

    @Nullable
    public static DeferredHolder<Block, DungeonAccessBlock> DUNGEON_KEY_BLOCK;


    public static void register() {
        RETURN_TO_OVERWORLD_BLOCK = registerBlock("return_to_overworld_block",
                () -> new ReturnToOverworldBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava()
                        .instrument(NoteBlockInstrument.BASS).strength(3.0F, 3.0F)
                        .noLootTable()));

        DUNGEON_BORDER = registerBlock("dungeon_border",
                () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava()
                        .instrument(NoteBlockInstrument.BASS).strength(-1.0F, 3600000.0F
                        ).noLootTable()));

        DUNGEON_KEY_BLOCK = registerBlock("dungeon_key_block",
                () -> new DungeonAccessBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava()
                        .instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F)
                        .requiresCorrectToolForDrops()));
    }
}
