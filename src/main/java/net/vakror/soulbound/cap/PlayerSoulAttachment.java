package net.vakror.soulbound.cap;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerSoulAttachment implements INBTSerializable<CompoundTag> {
    private int soul;
    private int darkSoul;
    public final int MIN_SOUL = 0;

    private int maxSoul = 100;
    private int maxDarkSoul = 100;

    public int getMaxSoul() {
        return maxSoul;
    }
    public int getSoulRegenPerSecond() {
        return soulRegenPerSecond;
    }

    private int soulRegenPerSecond = 0;
    private int darkSoulRegenPerSecond = 0;

    public void setDarkSoul(int darkSoul) {
        this.darkSoul = darkSoul;
    }

    public void setMaxDarkSoul(int maxDarkSoul) {
        this.maxDarkSoul = maxDarkSoul;
    }

    public void setDarkSoulRegenPerSecond(int darkSoulRegenPerSecond) {
        this.darkSoulRegenPerSecond = darkSoulRegenPerSecond;
    }
    public int getMaxDarkSoul() {
        return maxDarkSoul;
    }
    public int getDarkSoulRegenPerSecond() {
        return darkSoulRegenPerSecond;
    }

    public int getDarkSoul() {
        return darkSoul;
    }

    public void addDarkSoul(int soulToAdd) {
        this.darkSoul = Math.min(darkSoul + soulToAdd, maxDarkSoul);
    }

    public void subtractDarkSoul(int soulToSubtract) {
        this.darkSoul = Math.max(darkSoul - soulToSubtract, MIN_SOUL);
    }

    public void setSoul(int soul) {
        this.soul = soul;
    }

    public void setMaxSoul(int maxSoul) {
        this.maxSoul = maxSoul;
    }

    public void setSoulRegenPerSecond(int soulRegenPerSecond) {
        this.soulRegenPerSecond = soulRegenPerSecond;
    }

    public int getSoul() {
        return soul;
    }

    public void addSoul(int soulToAdd) {
        this.soul = Math.min(soul + soulToAdd, maxSoul);
    }

    public void copyFrom(PlayerSoulAttachment source) {
        this.darkSoul = source.darkSoul / 2;
        this.soul = source.soul / 2;
        this.soulRegenPerSecond = source.soulRegenPerSecond;
        this.darkSoulRegenPerSecond = source.darkSoulRegenPerSecond;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("soul", soul);
        nbt.putInt("max_soul", maxSoul);
        nbt.putInt("soul_regen", soulRegenPerSecond);
        nbt.putInt("dark_soul", darkSoul);
        nbt.putInt("max_dark_soul", maxDarkSoul);
        nbt.putInt("dark_soul_regen", darkSoulRegenPerSecond);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        soul = nbt.getInt("soul");
        maxSoul = nbt.getInt("max_soul");
        soulRegenPerSecond = nbt.getInt("soul_regen");
        darkSoul = nbt.getInt("dark_soul");
        maxDarkSoul = nbt.getInt("max_dark_soul");
        darkSoulRegenPerSecond = nbt.getInt("dark_soul_regen");
    }

}
