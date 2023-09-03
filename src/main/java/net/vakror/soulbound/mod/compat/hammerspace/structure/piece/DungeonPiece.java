package net.vakror.soulbound.mod.compat.hammerspace.structure.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DefaultDungeonTypes;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DungeonType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonPiece extends TemplateStructurePiece {
    DungeonType type;
    private final StructureTemplateManager manager;

    public DungeonPiece(StructurePieceType pType, int pGenDepth, StructureTemplateManager manager, ResourceLocation pLocation, String pTemplateName, StructurePlaceSettings pPlaceSettings, BlockPos pos, DungeonType type) {
        super(pType, pGenDepth, manager, pLocation, pTemplateName, pPlaceSettings, pos);
        this.manager = manager;
    }

    private DungeonPiece(StructureTemplateManager manager, DungeonType type, ResourceLocation templateName, BlockPos templatePos, @Nullable ConfiguredFeature<?, ?> tree, Rotation rotation, Mirror mirror) {
        super(ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get(), 0, manager, templateName, templateName.toString(), makeSettings(rotation, mirror, BlockIgnoreProcessor.STRUCTURE_BLOCK), templatePos);
        this.type = type;
        this.manager = manager;
    }

    public DungeonPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
        super(ModDungeonPieces.DEFAULT_DUNGEON_PIECE.get(), nbt, context.structureTemplateManager(), (con) -> makeSettings(Rotation.valueOf(nbt.getString("Rotation")), Mirror.valueOf(nbt.getString("Mirror")), BlockIgnoreProcessor.STRUCTURE_BLOCK));
        this.type = DungeonType.getTypeFromId(nbt.getString("Type"));
        this.manager = context.structureTemplateManager();
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation, Mirror mirror, StructureProcessor processor) {
        return new StructurePlaceSettings().setIgnoreEntities(true).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).addProcessor(processor).setRotation(rotation).setMirror(mirror);
    }

    public static void generateDungeon(BlockPos pos, Rotation rotation, StructureTemplateManager manager, StructurePiecesBuilder builder, int size, int layer, @Nullable DungeonPiece eventPiece, @NotNull DungeonType type, Level level, ResourceLocation file) {
        if (eventPiece == null) {
            StructurePlaceSettings settings = makeSettings(rotation, Mirror.NONE, new RuleProcessor(type.rules()));
            DungeonPiece piece = new DungeonPiece(type.structurePiece(), 128, manager, file, file.getPath(), settings, pos, type);
            builder.addPiece(piece);
        } else {
            builder.addPiece(eventPiece);
        }
    }

    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        pTag.putString("theme", this.type.id());
        super.addAdditionalSaveData(pContext, pTag);
    }

    protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
    }
}
