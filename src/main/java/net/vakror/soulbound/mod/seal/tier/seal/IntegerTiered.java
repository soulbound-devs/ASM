package net.vakror.soulbound.mod.seal.tier.seal;

public interface IntegerTiered extends Tiered {
    /**
     *
     * @return the amount of whatever the tier affects (eg: mining speed, damage)
     */
    int getAmount();
}
