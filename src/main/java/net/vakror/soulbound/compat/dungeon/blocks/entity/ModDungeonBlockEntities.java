package net.vakror.soulbound.compat.dungeon.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import org.jetbrains.annotations.Nullable;

import static net.vakror.soulbound.block.entity.ModBlockEntities.BLOCK_ENTITIES;

public class ModDungeonBlockEntities {
    @Nullable
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DungeonAccessBlockEntity>> DUNGEON_ACCESS_BLOCK_ENTITY;

    @Nullable
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ReturnToOverWorldBlockEntity>> RETURN_TO_OVERWORLD_BLOCK_ENTITY;

    public static void register() {
        DUNGEON_ACCESS_BLOCK_ENTITY = BLOCK_ENTITIES.register("dungeon_access_block_entity",
                () -> BlockEntityType.Builder.of(DungeonAccessBlockEntity::new, ModDungeonBlocks.DUNGEON_KEY_BLOCK.get())
                        .build(null));

        RETURN_TO_OVERWORLD_BLOCK_ENTITY = BLOCK_ENTITIES.register("return_to_overworld_block_entity",
                () -> BlockEntityType.Builder.of(ReturnToOverWorldBlockEntity::new, ModDungeonBlocks.RETURN_TO_OVERWORLD_BLOCK.get())
                        .build(null));
    }
}
