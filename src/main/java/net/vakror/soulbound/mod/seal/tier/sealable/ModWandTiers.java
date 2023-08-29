package net.vakror.soulbound.mod.seal.tier.sealable;

import net.minecraft.util.FastColor;

public enum ModWandTiers implements ISealableTier {
    ANCIENT_OAK(0, 1, 1, 2, FastColor.ARGB32.color(255, 20, 127, 199)),
    SACK(0, 0, 0, 4, FastColor.ARGB32.color(255, 163, 166, 168)),
    BLOOD_SOUL_SACK(1, 0, 0, 8, FastColor.ARGB32.color(255, 163, 166, 168)),
    WARPED_SACK(2, 0, 0, 15, FastColor.ARGB32.color(255, 163, 166, 168)),
    PURPUR_SACK(5, 0, 0, 24, FastColor.ARGB32.color(255, 163, 166, 168));

    private final int passiveSlots;
    private final int attackSlots;
    private final int amplificationSlots;
    private final int tier;
    private final int color;
    ModWandTiers(int tier, int passiveSlots, int attackSlots, int amplificationSlots, int color) {
        this.tier = tier;
        this.passiveSlots = passiveSlots;
        this.attackSlots = attackSlots;
        this.amplificationSlots = amplificationSlots;
        this.color = color;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getActivatableSlots() {
        return passiveSlots + attackSlots;
    }

    @Override
    public int getPassiveSlots() {
        return passiveSlots;
    }

    @Override
    public int getAttackSlots() {
        return attackSlots;
    }

    @Override
    public int getAmplificationSlots() {
        return amplificationSlots;
    }
}
