package net.vakror.soulbound.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.ModBlocks;
import net.vakror.soulbound.block.entity.custom.SoulCatalystBlockEntity;
import net.vakror.soulbound.block.entity.custom.SoulExtractorBlockEntity;
import net.vakror.soulbound.block.entity.custom.SoulSolidifierBlockEntity;
import net.vakror.soulbound.block.entity.custom.WandImbuingTableBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SoulboundMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WandImbuingTableBlockEntity>> WAND_IMBUING_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wand_imbuing_table_block_entity", () -> BlockEntityType.Builder.of(WandImbuingTableBlockEntity::new, ModBlocks.WAND_IMBUING_TABLE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulCatalystBlockEntity>> SOUL_CATALYST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_catalyst_block_entity", () -> BlockEntityType.Builder.of(SoulCatalystBlockEntity::new, ModBlocks.SOUL_CATALYST.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulExtractorBlockEntity>> SOUL_EXTRACTOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_extractor_block_entity", () -> BlockEntityType.Builder.of(SoulExtractorBlockEntity::new, ModBlocks.SOUL_EXTRACTOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulSolidifierBlockEntity>> SOUL_SOLIDIFIER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_solidifier_block_entity", () -> BlockEntityType.Builder.of(SoulSolidifierBlockEntity::new, ModBlocks.SOUL_SOLIDIFIER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
