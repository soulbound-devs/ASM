package net.vakror.soulbound.compat.hammerspace.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.vakror.soulbound.compat.hammerspace.dungeon.capability.Dungeon;
import net.vakror.soulbound.compat.hammerspace.dungeon.capability.DungeonProvider;
import net.vakror.soulbound.compat.hammerspace.structure.piece.DungeonPiece;
import net.vakror.soulbound.compat.hammerspace.structure.type.DungeonType;
import net.vakror.soulbound.compat.hammerspace.structure.util.DungeonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DungeonStructure extends Structure {
    public static final Codec<DungeonStructure> CODEC = simpleCodec(DungeonStructure::new);

    public final int size;
    public final int layer;
    public final int y;
    public final DungeonPiece piece;
    public final DungeonType type;
    public final Level level;
    public final ResourceLocation file;

    public DungeonStructure(Structure.StructureSettings settings) {
        super(settings);
        this.size = 50;
        this.layer = 0;
        this.y = 62;
        this.piece = null;
        this.type = DungeonType.getRandomType();
        this.level = null;
        file = type.getFile(size, layer, RandomSource.create());
    }

    public DungeonStructure(Structure.StructureSettings settings, int size, int layer, DungeonPiece piece, DungeonType type, Level level) {
        super(settings);
        this.size = size;
        this.layer = layer;
        this.y = 62 + (layer * 10);
        this.piece = piece;
        this.type = type;
        this.level = level;
        file = type.getFile(size, layer, RandomSource.create());
    }

    @Override
    public @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
//        BlockPos blockPos = new BlockPos(-25 + DungeonUtil.getXOffsetForSize(size), y, -25 + DungeonUtil.getZOffsetForSize(size));
        BlockPos blockPos = this.layer == 0 ? DungeonUtil.getGenerationPoint(file, y): this.level == null ? BlockPos.ZERO: this.level.getCapability(DungeonProvider.DUNGEON).orElse(new Dungeon()).getPos();
        return Optional.of(new Structure.GenerationStub(blockPos, (builder) -> {
            this.generatePieces(blockPos, Rotation.NONE, context.structureTemplateManager(), builder);
        }));
    }

    public void generatePieces(BlockPos pos, Rotation rot, StructureTemplateManager manager, StructurePiecesBuilder builder) {
        DungeonPiece.generateDungeon(pos, rot, manager, builder, this.size, this.layer, this.piece, type, level, this.file);
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.DUNGEON_TYPE.get();
    }
}
