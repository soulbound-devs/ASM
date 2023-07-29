package net.vakror.soulbound.compat.hammerspace.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;

import java.util.Map;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REGISTRY = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, SoulboundMod.MOD_ID);

    public static final RegistryObject<StructureType<DungeonStructure>> DUNGEON_TYPE = registerStructure("dungeon", DungeonStructure.CODEC);

    public static Structure.StructureSettings structure() {
        return new Structure.StructureSettings(HolderSet.direct(Holder.direct(RegistryAccess.fromRegistryOfRegistries(BuiltinRegistries.REGISTRY).registryOrThrow(Registry.BIOME_REGISTRY).getOrThrow(Biomes.PLAINS))), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
    }

    private static <S extends Structure> RegistryObject<StructureType<S>> registerStructure(String name, Codec<S> pCodec) {
        return STRUCTURE_TYPE_REGISTRY.register(name, () -> () -> pCodec);
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPE_REGISTRY.register(eventBus);
    }
}