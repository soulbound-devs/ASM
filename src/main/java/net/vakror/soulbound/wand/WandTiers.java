package net.vakror.soulbound.wand;

public enum WandTiers implements IWandTier{
    ANCIENT_OAK(1, 1, 2);

    private final int passiveSlots;
    private final int attackSlots;
    private final int amplificationSlots;

    WandTiers(int passiveSlots, int attackSlots, int amplificationSlots) {
        this.passiveSlots = passiveSlots;
        this.attackSlots = attackSlots;
        this.amplificationSlots = amplificationSlots;
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
