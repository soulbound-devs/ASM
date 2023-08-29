package net.vakror.soulbound.mod.util;

public class ColorUtil {

    public static int toColorInt(int R, int G, int B, int A) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}
