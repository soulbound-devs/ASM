package net.vakror.soulbound.client;

public class ClientSoulData {
    private static int playerSoul;
    private static long playerMaxSoul = 100;
    private static int darkPlayerSoul;
    private static long darkPlayerMaxSoul = 100;

    public static int getPlayerSoul() {
        return playerSoul;
    }

    public static int getDarkPlayerSoul() {
        return darkPlayerSoul;
    }

    public static long getPlayerMaxSoul() {
        return playerMaxSoul;
    }
    public static long getDarkPlayerMaxSoul() {
        return darkPlayerMaxSoul;
    }

    public static void set(int soul, int darkSoul, int maxSoul, int darkMaxSoul) {
        ClientSoulData.playerSoul = soul;
        ClientSoulData.darkPlayerSoul = darkSoul;
        ClientSoulData.playerMaxSoul = maxSoul;
        ClientSoulData.darkPlayerMaxSoul = darkMaxSoul;
    }
}
