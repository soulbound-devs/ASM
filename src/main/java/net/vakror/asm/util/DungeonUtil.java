package net.vakror.asm.util;

import net.minecraft.core.BlockPos;

import java.util.Random;

public class DungeonUtil {

    public static int getXOffsetForSize(int size, int rand) {
        return switch (size) {
            case 75 -> 50;
            case 100 -> 75;
            case 125 -> 100;
            default -> 20  + randomX(rand);
        };
    }

    private static int randomX(int rand) {
        return switch (rand) {
            case 0 -> -20;
            case 1 -> -14;
            case 2 -> -32;
            case 3 -> -23;
            case 4 -> -18;
            case 5 -> -33;
            case 6 -> -4;
            case 7 -> -11;
            case 8 -> -42;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
    }

    public static int getZOffsetForSize(int size, int rand) {
        return switch (size) {
            case 75 -> 50;
            case 100 -> 75;
            case 125 -> 100;
            default -> 17 + randomZ(rand);
        };
    }

    private static int randomZ(int rand) {
        return switch (rand) {
            case 0 -> -15;
            case 1 -> -7;
            case 2 -> -32;
            case 3 -> -40;
            case 4 -> 2;
            case 5 -> -5;
            case 6 -> -37;
            case 7 -> -24;
            case 8 -> -19;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
    }

    public static BlockPos getGenerationPoint(int size, int y) {
        int rand = new Random().nextInt(9);
        System.out.println("Dungeon Spawn location: " + getXOffsetForSize(size, rand) + ", " + getZOffsetForSize(size, rand));
        return new BlockPos(-25 + getXOffsetForSize(size, rand), y, -25 + getZOffsetForSize(size, rand));
    }
}
