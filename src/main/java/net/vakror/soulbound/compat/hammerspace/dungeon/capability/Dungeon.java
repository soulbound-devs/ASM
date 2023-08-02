package net.vakror.soulbound.compat.hammerspace.dungeon.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.vakror.soulbound.compat.hammerspace.dungeon.level.DungeonLevel;
import net.vakror.soulbound.compat.hammerspace.structure.type.DungeonType;

public class Dungeon {
    private boolean isStable;
    private int levelsBeaten;
    private DungeonLevel currentLevel;
    private int maxLevels;
    private boolean canEnter = true;
    private int levelsGenerated = 1;
    public DungeonType type;
    public BlockPos pos;
    public void copyFrom(Dungeon source) {
        this.isStable = source.isStable;
        this.levelsBeaten = source.levelsBeaten;
        this.canEnter = source.canEnter;
    }

    public void beatCurrentLevel() {
        levelsBeaten++;
    }

    public boolean isStable() {
        return isStable;
    }

    public int getLevelsGenerated() {
        return levelsGenerated;
    }

    public void setLevelsGenerated(int levelsGenerated) {
        this.levelsGenerated = levelsGenerated;
    }

    public void setStable(boolean stable) {
        isStable = stable;
    }

    public boolean canEnter() {
        return canEnter;
    }

    public int getLevelsBeaten() {
        return levelsBeaten;
    }

    public void setLevelsBeaten(int levelsBeaten) {
        this.levelsBeaten = levelsBeaten;
    }

    public DungeonType getType() {
        return type;
    }

    public Dungeon setType(DungeonType type) {
        this.type = type;
        return this;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("stable", isStable);
        nbt.putBoolean("canEnter", canEnter);
        nbt.putInt("levelsBeaten", levelsBeaten);
        if (currentLevel != null) {
            nbt.put("currentLevel", currentLevel.serializeNbt());
        }
        nbt.putInt("maxLevels", maxLevels);
        nbt.putInt("levelsGenerated", levelsGenerated);
        if (type != null) {
            nbt.putInt("type", type.getIndex());
        } if (pos != null) {
            nbt.put("pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get());
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        isStable = nbt.getBoolean("stable");
        canEnter = nbt.getBoolean("canEnter");
        levelsBeaten = nbt.getInt("levelsBeaten");
        if (nbt.contains("currentLevel")) {
            currentLevel = new DungeonLevel(0, 0).deserializeNbt(nbt.getCompound("currentLevel"));
        }
        maxLevels = nbt.getInt("maxLevels");
        levelsGenerated = nbt.getInt("levelsGenerated");
        if (nbt.contains("type")) {
            type = DungeonType.getTypeFromIndex(nbt.getInt("type"));
        } if (nbt.contains("pos")) {
            pos = BlockPos.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("pos")).resultOrPartial((error) -> {throw new IllegalStateException(error);}).get();
        }
    }

    public DungeonLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(DungeonLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }
    public void setCanEnter(boolean canEnter) {
        this.canEnter = canEnter;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Dungeon setPos(BlockPos pos) {
        this.pos = pos;
        return this;
    }
}