package net.vakror.asm.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;

import java.util.Map;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REGISTRY = DeferredRegister.create(Registries.STRUCTURE_TYPE, ASMMod.MOD_ID);

    public static final RegistryObject<StructureType<DungeonStructure>> DUNGEON_TYPE = registerStructure("dungeon", DungeonStructure.CODEC);

    public static Structure.StructureSettings structure() {
        return new Structure.StructureSettings(HolderSet.direct(), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
    }

    private static <S extends Structure> RegistryObject<StructureType<S>> registerStructure(String name, Codec<S> pCodec) {
        return STRUCTURE_TYPE_REGISTRY.register(name, () -> () -> pCodec);
    }

    public static void bootstrapSets(BootstapContext<StructureSet> bootstrapContext) {
        HolderGetter<Structure> holdergetter = bootstrapContext.lookup(Registries.STRUCTURE);
        bootstrapContext.register(ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(ASMMod.MOD_ID, "dungeon")), new StructureSet(holdergetter.getOrThrow(ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(ASMMod.MOD_ID, "dungeon"))), new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 45234535)));
    }

    public static void bootstrap(BootstapContext<Structure> bootstrapContext) {
        bootstrapContext.register(ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(ASMMod.MOD_ID, "dungeon")), new DungeonStructure(structure()));
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPE_REGISTRY.register(eventBus);
    }
}