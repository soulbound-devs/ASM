package net.vakror.soulbound.seal.tier;

public interface SealWithAmount {
    /**
     * @return the amount of whatever the tier affects (eg: mining speed, damage)
     */
    float getAmount(int tier);
}
