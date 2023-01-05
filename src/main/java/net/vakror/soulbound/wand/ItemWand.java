package net.vakror.soulbound.wand;

import net.minecraft.nbt.CompoundTag;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemWand {
    private List<ISeal> passiveSeals = null;
    private List<ISeal> attackSeals = null;
    private List<ISeal> amplifyingSeals = null;

    private ISeal activeSeal = null;

    public List<ISeal> getPassiveSeals() {
        createIfNull();
        return passiveSeals;
    }

    public ISeal getActiveSeal() {
        return activeSeal;
    }

    public void setActiveSeal(ISeal seal) {
        activeSeal = seal;
    }

    public List<ISeal> getAttackSeals() {
        createIfNull();
        return attackSeals;
    }

    public List<ISeal> getAmplifyingSeals() {
        createIfNull();
        return amplifyingSeals;
    }

    public void addPassiveSeal(String id) {
        createIfNull();
        passiveSeals.add(SealRegistry.allSeals.get(id));
    }

    public void addAttackSeal(String id) {
        createIfNull();
        attackSeals.add(SealRegistry.allSeals.get(id));
    }

    public void addAmplifyingSeal(String id) {
        createIfNull();
        amplifyingSeals.add(SealRegistry.allSeals.get(id));
    }

    public void createIfNull() {
        if (this.passiveSeals == null) {
            passiveSeals = new ArrayList<>();
        }
        if (this.attackSeals == null) {
            attackSeals = new ArrayList<>();
        }
        if (this.amplifyingSeals == null) {
            amplifyingSeals = new ArrayList<>();
        }
    }

    public void setPassiveSeals(List<ISeal> passiveSeals) {
        this.passiveSeals = passiveSeals;
    }

    public void setAttackSeals(List<ISeal> attackSeals) {
        this.attackSeals = attackSeals;
    }

    public void setAmplifyingSeals(List<ISeal> amplifyingSeals) {
        this.amplifyingSeals = amplifyingSeals;
    }


    public void copyFrom(ItemWand source) {
        this.passiveSeals = source.passiveSeals;
        this.attackSeals = source.attackSeals;
        this.amplifyingSeals = source.amplifyingSeals;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (this.passiveSeals != null && this.attackSeals != null && this.amplifyingSeals != null) {
            for (String sealId : SealRegistry.passiveSeals.keySet()) {
                nbt.putBoolean(sealId, passiveSeals.contains(SealRegistry.allSeals.get(sealId)));
            }
            for (String sealId : SealRegistry.attackSeals.keySet()) {
                nbt.putBoolean(sealId, attackSeals.contains(SealRegistry.allSeals.get(sealId)));
            }
            for (String sealId : SealRegistry.amplifyingSeals.keySet()) {
                nbt.putBoolean(sealId, amplifyingSeals.contains(SealRegistry.allSeals.get(sealId)));
            }
            if (activeSeal != null) {
                nbt.putString("active_seal", activeSeal.getId());
            }
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        createIfNull();
        for (String sealId : SealRegistry.passiveSeals.keySet()) {
            if (nbt.getBoolean(sealId)) {
                passiveSeals.add(SealRegistry.passiveSeals.get(sealId));
            }
        }
        for (String sealId : SealRegistry.attackSeals.keySet()) {
            if (nbt.getBoolean(sealId)) {
                attackSeals.add(SealRegistry.attackSeals.get(sealId));
            }
        }
        for (String sealId : SealRegistry.amplifyingSeals.keySet()) {
            if (nbt.getBoolean(sealId)) {
                amplifyingSeals.add(SealRegistry.amplifyingSeals.get(sealId));
            }
        }
        activeSeal = SealRegistry.allSeals.get(nbt.getString("active_seal"));
    }
}