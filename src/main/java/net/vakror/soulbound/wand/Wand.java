package net.vakror.soulbound.wand;

import net.minecraft.nbt.CompoundTag;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;

import java.util.List;

public class Wand {

    private List<ISeal> seals;

    public List<ISeal> getSeals() {
        return seals;
    }

    public void addSeal(String id) {
        seals.add(SealRegistry.seals.get(id));
    }

    public void removeSeal(String id) {
        seals.remove(SealRegistry.seals.get(id));
    }

    public void setSeals(List<ISeal> seals) {
        this.seals = seals;
    }

    public void copyFrom(Wand source) {
        this.seals = source.seals;
    }

    public void saveNBTData(CompoundTag nbt) {
        for (String sealId: SealRegistry.seals.keySet()) {
            nbt.putBoolean(sealId, seals.contains(SealRegistry.seals.get(sealId)));
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        for (String sealId: SealRegistry.seals.keySet()) {
            if (nbt.getBoolean(sealId)) {
                seals.add(SealRegistry.seals.get(sealId));
            }
        }
    }

}















