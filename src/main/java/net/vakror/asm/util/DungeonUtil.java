package net.vakror.asm.util;

public class DungeonUtil {

    public static int getXOffsetForSize(int size) {
        return switch (size) {
            case 75 -> 50;
            case 100 -> 75;
            case 125 -> 100;
            default -> 20;
        };
    }

    public static int getZOffsetForSize(int size) {
        return switch (size) {
            case 75 -> 50;
            case 100 -> 75;
            case 125 -> 100;
            default -> 17;
        };
    }
}
