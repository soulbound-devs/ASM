package net.vakror.asm.world.structure;

import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum DungeonTheme {
    DEFAULT("default", new ArrayList(), 0);

    String name;
    List<Block> themeBlocks;
    int index;

    private DungeonTheme(String name, List themeBlocks, int index) {
        this.name = name;
        this.themeBlocks = themeBlocks;
        this.index = index;
    }

    public String id() {
        return this.name;
    }

    public List<Block> getThemeBlocks() {
        return this.themeBlocks;
    }

    public int getIndex() {
        return this.index;
    }

    public static DungeonTheme getThemeFromIndex(int index) {
        DungeonTheme[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            DungeonTheme theme = var1[var3];
            if (theme.getIndex() == index) {
                return theme;
            }
        }

        return DEFAULT;
    }

    public static DungeonTheme getRandomTheme() {
        int rand = (new Random()).nextInt(values().length);
        DungeonTheme[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            DungeonTheme theme = var1[var3];
            if (theme.getIndex() == rand) {
                return theme;
            }
        }

        throw new IllegalStateException("Random theme failed: index = " + rand);
    }
}
