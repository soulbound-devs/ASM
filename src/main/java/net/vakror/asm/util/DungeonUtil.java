package net.vakror.asm.util;

import net.minecraft.core.BlockPos;

import java.util.Random;

public class DungeonUtil {

    public static int getXOffsetForSize(int size, int rand) {
        return switch (size) {
            case 75 -> 50;
            case 100 -> 75;
            case 125 -> 100;
            default -> 20 + randomX(rand);
        };
    }

    private static int randomX(int rand) {
        return switch (rand) {
            case 1 -> -20;
            case 2 -> -14;
            case 3 -> -32;
            case 4 -> -23;
            case 5 -> -18;
            case 6 -> -33;
            case 7 -> -4;
            case 8 -> -11;
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
            case 1 -> -15;
            case 2 -> -7;
            case 3 -> -32;
            case 4 -> -40;
            case 5 -> 2;
            case 6 -> -5;
            case 7 -> -37;
            case 8 -> -24;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
    }

    public static BlockPos getGenerationPoint(int size, int y) {
        int rand = new Random().nextInt(11);
        return new BlockPos(-25 + getXOffsetForSize(size, rand), y, -25 + getZOffsetForSize(size, rand));
    }
}
