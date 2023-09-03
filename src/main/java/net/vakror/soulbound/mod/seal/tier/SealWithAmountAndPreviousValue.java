package net.vakror.soulbound.mod.seal.tier;

public interface SealWithAmountAndPreviousValue {
    /**
     *
     * @return the amount of whatever the tier affects (eg: mining speed, damage)
     * @param tier will be zero if not instanceof Tiered
     */
    float getAmount(int tier, float previousValue);
}
