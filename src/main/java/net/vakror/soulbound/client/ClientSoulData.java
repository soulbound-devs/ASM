package net.vakror.soulbound.client;

public class ClientSoulData {
    private static int playerSoul;
    private static long playerMaxSoul = 100;

    public static int getPlayerSoul() {
        return playerSoul;
    }

    public static long getPlayerMaxSoul() {
        return playerMaxSoul;
    }

    public static void set(int soul) {
        ClientSoulData.playerSoul = soul;
    }

    public static void setMax(long max) {
        ClientSoulData.playerMaxSoul = max;
    }
}
