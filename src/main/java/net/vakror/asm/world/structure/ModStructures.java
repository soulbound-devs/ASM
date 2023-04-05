package net.vakror.asm.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;

import java.util.Map;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REGISTRY = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, ASMMod.MOD_ID);
    public static final DeferredRegister<StructureSet> STRUCTURE_SET_REGISTRY = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, ASMMod.MOD_ID);
    public static final DeferredRegister<Structure> STRUCTURE_REGISTRY = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, ASMMod.MOD_ID);
    public static final RegistryObject<StructureType<DungeonStructure>> DUNGEON_TYPE = registerStructure("dungeon", DungeonStructure.CODEC);

    public static final RegistryObject<Structure> DUNGEON = STRUCTURE_REGISTRY.register("dungeon", () -> new DungeonStructure(structure()));

    RegistryObject<StructureSet> DUNGEON_SET = registerSet("dungeon", DUNGEON.getHolder().get(), new RandomSpreadStructurePlacement(80, 20, RandomSpreadType.TRIANGULAR, 10387319));

    public static Structure.StructureSettings structure() {
        return new Structure.StructureSettings(HolderSet.direct(), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
    }

    private static <S extends Structure> RegistryObject<StructureType<S>> registerStructure(String name, Codec<S> pCodec) {
        return STRUCTURE_TYPE_REGISTRY.register(name, () -> () -> {
            return pCodec;
        });
    }

    private static <S extends Structure> RegistryObject<StructureSet> registerSet(String name, Holder<Structure> structure, StructurePlacement placement) {
        return STRUCTURE_SET_REGISTRY.register(name, () -> new StructureSet(structure, placement));
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPE_REGISTRY.register(eventBus);
        STRUCTURE_REGISTRY.register(eventBus);
        STRUCTURE_SET_REGISTRY.register(eventBus);
    }
}