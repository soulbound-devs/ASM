package net.vakror.soulbound.mod.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.blocks.ModBlocks;
import net.vakror.soulbound.mod.blocks.entity.custom.*;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SoulboundMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<WandImbuingTableBlockEntity>> WAND_IMBUING_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wand_imbuing_table_block_entity", () -> BlockEntityType.Builder.of(WandImbuingTableBlockEntity::new, ModBlocks.WAND_IMBUING_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SoulCatalystBlockEntity>> SOUL_CATALYST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_catalyst_block_entity", () -> BlockEntityType.Builder.of(SoulCatalystBlockEntity::new, ModBlocks.SOUL_CATALYST.get()).build(null));

    public static final RegistryObject<BlockEntityType<SoulExtractorBlockEntity>> SOUL_EXTRACTOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_extractor_block_entity", () -> BlockEntityType.Builder.of(SoulExtractorBlockEntity::new, ModBlocks.SOUL_EXTRACTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<SoulSolidifierBlockEntity>> SOUL_SOLIDIFIER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("soul_solidifier_block_entity", () -> BlockEntityType.Builder.of(SoulSolidifierBlockEntity::new, ModBlocks.SOUL_SOLIDIFIER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
