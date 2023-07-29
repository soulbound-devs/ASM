package net.vakror.soulbound.compat.hammerspace.structure;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;

import java.util.function.Supplier;

public class ModDungeonPieces {
    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, SoulboundMod.MOD_ID);
    public static final RegistryObject<StructurePieceType> DEFAULT_DUNGEON_PIECE = register("default", () -> DungeonPiece::new);

    public ModDungeonPieces() {
    }

    public static RegistryObject<StructurePieceType> register(String name, Supplier<StructurePieceType> type) {
        return STRUCTURE_PIECE.register(name, type);
    }

    public static void register(IEventBus eventBus) {
        STRUCTURE_PIECE.register(eventBus);
    }
}
