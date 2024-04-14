package net.vakror.soulbound.compat.dungeon.setup;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.Vec3;
import net.vakror.soulbound.compat.dungeon.theme.DungeonTheme;
import net.vakror.soulbound.compat.dungeon.theme.ThemeList;
import net.vakror.soulbound.extension.dungeon.structure.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public abstract class StructureDungeonSetup extends DungeonSetup {

    public @NotNull List<ThemeList> getThemes(ServerLevel level) {
        return List.of();
    }

    public static Random random = new Random();

    @Override
    public State place(ServerLevel level, RegistryAccess access) {
        try {
            DungeonStructure structure = getStructure(level);
            if (structure == null) {
                throw new IllegalStateException("Error while placing dungeon structure!");
            }
            List<ThemeList> themeLists = getThemes(level);
            if (!themeLists.isEmpty()) {
                ThemeList themeList = themeLists.get(random.nextInt(themeLists.size()));
                structure.processors.clear();
                for (DungeonTheme theme : themeList.themeList()) {
                    structure.addProcessor(theme.getProcessor());
                }
            }
            ChunkGenerator chunkgenerator = level.getChunkSource().getGenerator();
            assert structure != null;
            StructureStart structureStart = structure.generate(access, chunkgenerator, chunkgenerator.getBiomeSource(), level.getChunkSource().randomState(), level.getStructureManager(), level.getSeed(), getDungeonPos(), 0, level, (biome) -> true);
            if (!structureStart.isValid()) {
                throw new IllegalStateException("Invalid Structure Start for Dungeon");
            } else {
                BoundingBox boundingbox = structureStart.getBoundingBox();
                ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
                ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
                if (!checkLoaded(level, chunkpos, chunkpos1)) {
                    return State.RETRY;
                }
                ChunkPos.rangeClosed(chunkpos, chunkpos1).forEach((p_214558_) -> {
                    structureStart.placeInChunk(level, level.structureManager(), chunkgenerator, level.getRandom(), new BoundingBox(p_214558_.getMinBlockX(), level.getMinBuildHeight(), p_214558_.getMinBlockZ(), p_214558_.getMaxBlockX(), level.getMaxBuildHeight(), p_214558_.getMaxBlockZ()), p_214558_);
                });
            }
        } catch (Exception e) {
            State.FAIL.setException(e);
            return State.FAIL;
        }
        return State.SUCCESS;
    }

    public static boolean checkLoaded(ServerLevel pLevel, ChunkPos pStart, ChunkPos pEnd) {
        return ChunkPos.rangeClosed(pStart, pEnd).allMatch((pos) -> pLevel.isLoaded(pos.getWorldPosition()));
    }

    public ChunkPos getDungeonPos() {
        return new ChunkPos(0, 0);
    }

    public abstract DungeonStructure getStructure(ServerLevel level);

    public DungeonStructure getStructureFromResourceLocation(ResourceLocation location, ServerLevel level) {
        return getStructureFromResourceLocation(location, level.registryAccess());
    }

    public DungeonStructure getStructureFromResourceLocation(ResourceLocation resourceLocation, RegistryAccess registryAccess) {
        return (DungeonStructure) registryAccess.registryOrThrow(Registries.STRUCTURE).get(resourceLocation);
    }
}
