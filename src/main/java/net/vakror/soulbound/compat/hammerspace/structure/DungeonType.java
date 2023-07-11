package net.vakror.soulbound.compat.hammerspace.structure;

import java.util.Random;

public enum DungeonType {
    DEFAULT("default", 0);

    String name;
    int index;

    private DungeonType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String id() {
        return this.name;
    }


    public int getIndex() {
        return this.index;
    }

    public static DungeonType getTypeFromIndex(int index) {
        DungeonType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            DungeonType theme = var1[var3];
            if (theme.getIndex() == index) {
                return theme;
            }
        }

        return DEFAULT;
    }

    public static DungeonType getRandomType() {
        int rand = (new Random()).nextInt(values().length);
        DungeonType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            DungeonType theme = var1[var3];
            if (theme.getIndex() == rand) {
                return theme;
            }
        }

        throw new IllegalStateException("Random theme failed: index = " + rand);
    }
}
