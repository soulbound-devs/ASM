package net.vakror.asm.dungeon.capability;

import net.minecraft.nbt.CompoundTag;
import net.vakror.asm.dungeon.DungeonLevel;

public class Dungeon {
    private boolean isStable;
    private int levelsBeaten;
    private DungeonLevel currentLevel;
    private int maxLevels;
    private boolean canEnter = true;
    private int levelsGenerated = 1;
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

    public void saveNBTData(CompoundTag nbt) {
        nbt.putBoolean("stable", isStable);
        nbt.putBoolean("canEnter", canEnter);
        nbt.putInt("levelsBeaten", levelsBeaten);
        if (currentLevel != null) {
            nbt.put("currentLevel", currentLevel.serializeNbt());
        }
        nbt.putInt("maxLevels", maxLevels);
        nbt.putInt("levelsGenerated", levelsGenerated);
    }

    public void loadNBTData(CompoundTag nbt) {
        isStable = nbt.getBoolean("stable");
        canEnter = nbt.getBoolean("canEnter");
        levelsBeaten = nbt.getInt("levelsBeaten");
        if (nbt.contains("currentLevel")) {
            currentLevel = DungeonLevel.deserializeNbt(nbt.getCompound("currentLevel"));
        }
        maxLevels = nbt.getInt("maxLevels");
        levelsGenerated = nbt.getInt("levelsGenerated");
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
}