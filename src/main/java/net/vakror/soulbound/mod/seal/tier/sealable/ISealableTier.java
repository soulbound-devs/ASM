package net.vakror.soulbound.mod.seal.tier.sealable;

public interface ISealableTier {
    int getPassiveSlots();
    int getAttackSlots();
    int getAmplificationSlots();
    int getActivatableSlots();
    int getTier();

    int getColor();
}
