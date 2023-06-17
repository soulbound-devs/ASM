package net.vakror.asm.seal.tier.seal;

public interface IntegerTiered extends Tiered {
    /**
     *
     * @return the amount of whatever the tier affects (eg: mining speed, damage)
     */
    public int getAmount();
}
