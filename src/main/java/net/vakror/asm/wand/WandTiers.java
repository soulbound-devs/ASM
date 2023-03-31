package net.vakror.asm.wand;

public enum WandTiers implements IWandTier {
    ANCIENT_OAK(0, 1, 1, 2);

    private final int passiveSlots;
    private final int attackSlots;
    private final int amplificationSlots;
    private final int tier;

    WandTiers(int tier, int passiveSlots, int attackSlots, int amplificationSlots) {
        this.tier = tier;
        this.passiveSlots = passiveSlots;
        this.attackSlots = attackSlots;
        this.amplificationSlots = amplificationSlots;
    }

    @Override
    public int getTier() {
        return tier;
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
