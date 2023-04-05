package net.vakror.asm.world.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.blocks.custom.DungeonAccessBlock;
import net.vakror.asm.blocks.entity.custom.DungeonAccessBlockEntity;
import net.vakror.asm.world.structure.DungeonStructure;
import net.vakror.asm.world.structure.ModStructures;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DungeonTeleporter implements ITeleporter {
    protected final ServerLevel level;
    protected final BlockPos pos;
    protected final DungeonAccessBlock block;

    public DungeonTeleporter(ServerLevel level, BlockPos pos, DungeonAccessBlock block) {
        this.level = level;
        this.pos = pos;
        this.block = block;
    }

    @Override
    public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        if (!(destWorld.getBlockEntity(pos) instanceof DungeonAccessBlockEntity)) {
            StructureStart start = new DungeonStructure(ModStructures.structure()).generate(destWorld.registryAccess(), destWorld.getChunkSource().getGenerator(), destWorld.getChunkSource().getGenerator().getBiomeSource(), destWorld.getChunkSource().randomState(), destWorld.getStructureManager(), destWorld.getSeed(), new ChunkPos(pos), 0, destWorld, (biomeHolder) -> {return true;});
            BoundingBox boundingbox = start.getBoundingBox();
            ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
            ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
            checkLoaded(destWorld, chunkpos, chunkpos1);
            ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((chunkPos) -> {
                start.placeInChunk(destWorld, destWorld.structureManager(), destWorld.getChunkSource().getGenerator(), destWorld.getRandom(), new BoundingBox(chunkPos.getMinBlockX(), destWorld.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), destWorld.getMaxBuildHeight(), chunkPos.getMaxBlockZ()), chunkPos);
            });
            destWorld.setBlock(pos, ModBlocks.DUNGEON_KEY_BLOCK.get().defaultBlockState().setValue(DungeonAccessBlock.TO_OVERWORLD, true), 3);
        }
        return new PortalInfo(new Vec3(pos.above().getX(), pos.above().getY(), pos.above().getZ()), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
    }

    public static void checkLoaded(ServerLevel pLevel, ChunkPos pStart, ChunkPos pEnd) {
        if (ChunkPos.rangeClosed(pStart, pEnd).anyMatch((pos) -> !pLevel.isLoaded(pos.getWorldPosition()))) {
            throw new IllegalStateException("Pos Not Loaded!");
        }
    }

    @Override
    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
        return false;
    }
}
