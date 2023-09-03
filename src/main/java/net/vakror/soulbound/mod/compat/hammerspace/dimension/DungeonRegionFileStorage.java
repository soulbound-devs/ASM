package net.vakror.soulbound.mod.compat.hammerspace.dimension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;

// ensure the dungeon dimensions only serialize the 16 chunks they have to reduce hard drive space per dungeon
public class DungeonRegionFileStorage extends RegionFileStorage {
	public DungeonRegionFileStorage(Path path, boolean sync) {
		super(path, sync);
	}

	@Override
	protected void write(ChunkPos pos, CompoundTag compound) throws IOException {
		if ((pos.x <= 4 && pos.x >= -4) && (pos.z <= 4 && pos.z >= -4)) {
			super.write(pos, compound);
		}
	}

	@Override
	@Nullable
	public CompoundTag read(ChunkPos pos) throws IOException {
		if ((pos.x <= 4 && pos.x >= -4) && (pos.z <= 4 && pos.z >= -4)) {
			return super.read(pos);
		}
		else {
			return null;
		}
	}
}