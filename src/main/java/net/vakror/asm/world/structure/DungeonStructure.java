package net.vakror.asm.world.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;
import java.util.function.Predicate;

public class DungeonStructure extends Structure {
    public static final Codec<DungeonStructure> CODEC = simpleCodec(DungeonStructure::new);

    public DungeonStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int startY = (new UniformHeight(VerticalAnchor.BOTTOM, VerticalAnchor.TOP)).sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), startY, chunkPos.getMinBlockZ());
        return Optional.of(new Structure.GenerationStub(blockPos, (builder) -> {
            this.generatePieces(builder, DungeonTheme.getRandomTheme());
        }));
    }

    public void generatePieces(StructurePiecesBuilder builder, DungeonTheme theme) {
    }

    public StructureStart generate(RegistryAccess pRegistryAccess, ChunkGenerator pChunkGenerator, BiomeSource pBiomeSource, RandomState pRandomState, StructureTemplateManager pStructureTemplateManager, long pSeed, ChunkPos pChunkPos, int p_226604_, LevelHeightAccessor pHeightAccessor, Predicate<Holder<Biome>> pValidBiome) {
        return super.generate(pRegistryAccess, pChunkGenerator, pBiomeSource, pRandomState, pStructureTemplateManager, pSeed, pChunkPos, p_226604_, pHeightAccessor, pValidBiome);
    }

    public StructureType<?> type() {
        return (StructureType)ModStructures.DUNGEON.get();
    }
}
