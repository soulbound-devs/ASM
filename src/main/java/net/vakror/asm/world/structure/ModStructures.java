package net.vakror.asm.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_REGISTRY;
    public static final RegistryObject<StructureType<DungeonStructure>> DUNGEON;

    public ModStructures() {
    }

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> {
            return structureCodec;
        };
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_REGISTRY.register(eventBus);
    }

    static {
        STRUCTURE_REGISTRY = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, "asm");
        DUNGEON = STRUCTURE_REGISTRY.register("dungeon", () -> {
            return explicitStructureTypeTyping(DungeonStructure.CODEC);
        });
    }
}
