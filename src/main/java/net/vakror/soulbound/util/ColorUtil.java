package net.vakror.soulbound.util;

import java.awt.*;

public class ColorUtil {
    public static Color overlayColor(Color base, Color overlay) {
        return new Color(ColorUtil.overlayColor(base.getRGB(), overlay.getRGB()), true);
    }

    public static int overlayColor(int base, int overlay) {
        int alpha = (base & 0xFF000000) >> 24;
        int baseR = (base & 0xFF0000) >> 16;
        int baseG = (base & 0xFF00) >> 8;
        int baseB = base & 0xFF;
        int overlayR = (overlay & 0xFF0000) >> 16;
        int overlayG = (overlay & 0xFF00) >> 8;
        int overlayB = overlay & 0xFF;
        int r = Math.round((float)baseR * ((float)overlayR / 255.0f)) & 0xFF;
        int g = Math.round((float)baseG * ((float)overlayG / 255.0f)) & 0xFF;
        int b = Math.round((float)baseB * ((float)overlayB / 255.0f)) & 0xFF;
        return alpha << 24 | r << 16 | g << 8 | b;
    }

    public static int toColorInt(int R, int G, int B, int A) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}
