package net.vakror.soulbound.mixin;

import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructurePiecesBuilder.class)
public interface StructurePiecesBuilderAccessor {
    @Accessor
    List<StructurePiece> getPieces();
}
