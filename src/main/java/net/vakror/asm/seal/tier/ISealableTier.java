package net.vakror.asm.seal.tier;

public interface ISealableTier {
    int getPassiveSlots();
    int getAttackSlots();
    int getAmplificationSlots();
    int getActivatableSlots();
    int getTier();
}
