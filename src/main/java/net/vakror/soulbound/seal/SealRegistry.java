package net.vakror.soulbound.seal;


import net.vakror.soulbound.seal.seals.Seal;

import java.util.HashMap;
import java.util.Map;

public class SealRegistry {
    public static Map<String, ISeal> seals = new HashMap<>();

    public static void registerSeals() {
        seals.put("axing", new Seal("axing"));
        seals.put("pickaxing", new Seal("pickaxing"));
        seals.put("hoeing", new Seal("hoeing"));
    }
}
