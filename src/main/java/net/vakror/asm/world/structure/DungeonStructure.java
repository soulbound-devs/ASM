package net.vakror.asm.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.vakror.asm.util.DungeonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DungeonStructure extends Structure {
    public static final Codec<DungeonStructure> CODEC = simpleCodec(DungeonStructure::new);

    public final int size;
    public final int layer;
    public final int y;

    public DungeonStructure(Structure.StructureSettings settings) {
        super(settings);
        this.size = 50;
        this.layer = 0;
        this.y = 62;
    }

    public DungeonStructure(Structure.StructureSettings settings, int size, int layer) {
        super(settings);
        this.size = size;
        this.layer = layer;
        this.y = 62 + (layer * 10);
    }

    @Override
    public @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
//        BlockPos blockPos = new BlockPos(-25 + DungeonUtil.getXOffsetForSize(size), y, -25 + DungeonUtil.getZOffsetForSize(size));
        BlockPos blockPos = DungeonUtil.getGenerationPoint(size, y);
        return Optional.of(new Structure.GenerationStub(blockPos, (builder) -> {
            this.generatePieces(blockPos, Rotation.NONE, context.structureTemplateManager(), builder);
        }));
    }

    public void generatePieces(BlockPos pos, Rotation rot, StructureTemplateManager manager, StructurePiecesBuilder builder) {
        DungeonPiece.generateDungeon(pos, rot, manager, builder, this.size, this.layer);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.DUNGEON_TYPE.get();
    }
}
