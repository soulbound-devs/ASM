package net.vakror.soulbound.seal;

import net.vakror.soulbound.seal.seals.MiningSeal;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> seals = new HashMap<>();

    public static void registerSeals() {
        seals.put("axing", new MiningSeal());
        seals.put("pickaxing", new MiningSeal());
        seals.put("hoeing", new MiningSeal());
    }
}
