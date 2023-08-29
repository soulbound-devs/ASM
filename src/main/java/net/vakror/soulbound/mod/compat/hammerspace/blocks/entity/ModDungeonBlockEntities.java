package net.vakror.soulbound.mod.compat.hammerspace.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.hammerspace.blocks.ModDungeonBlocks;

public class ModDungeonBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SoulboundMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<DungeonAccessBlockEntity>> DUNGEON_ACCESS_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("dungeon_access_block_entity", () -> BlockEntityType.Builder.of(DungeonAccessBlockEntity::new, ModDungeonBlocks.DUNGEON_KEY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ReturnToOverWorldBlockEntity>> RETURN_TO_OVERWORLD_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("return_to_overworld_block_entity", () -> BlockEntityType.Builder.of(ReturnToOverWorldBlockEntity::new, ModDungeonBlocks.RETURN_TO_OVERWORLD_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
