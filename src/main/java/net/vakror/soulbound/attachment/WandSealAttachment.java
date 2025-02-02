package net.vakror.soulbound.attachment;

import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.SealType;
import net.vakror.soulbound.seal.tier.seal.Tiered;

import java.util.*;

public class WandSealAttachment implements INBTSerializable<CompoundTag> {
    private List<ISeal> passiveSeals = null;
    private List<ISeal> attackSeals = null;
    private List<ISeal> amplifyingSeals = null;
    private int selectedSealSlot = 1;
    private boolean selectedIsAttack = false;
    ResourceLocation customWandModel = MissingTextureAtlasSprite.getLocation();
    private Map<ResourceLocation, ResourceLocation> activeSealModels = new HashMap<>();

    public boolean isSelectedIsAttack() {
        return selectedIsAttack;
    }

    public void setSelectedIsAttack(boolean selectedIsAttack) {
        this.selectedIsAttack = selectedIsAttack;
    }

    public int getSelectedSealSlot() {
        return selectedSealSlot;
    }

    public void setSelectedSealSlot(int selectedSealSlot) {
        this.selectedSealSlot = selectedSealSlot;
    }


    private ISeal activeSeal = null;

    public List<ISeal> getPassiveSeals() {
        createIfNull();
        return passiveSeals;
    }

    public Set<ISeal> getUniquePassiveSeals() {
        createIfNull();
        return Set.copyOf(passiveSeals);
    }

    public ISeal getActiveSeal() {
        return activeSeal;
    }

    public void setActiveSeal(ISeal seal, ItemStack stack) {
        activeSeal = seal;
        CompoundTag tag = stack.getTag().copy();
        tag.putString("activeSeal", activeSeal == null ? "": activeSeal.getId().toString());
        stack.setTag(tag);
    }

    public List<ISeal> getAllActivatableSeals() {
        List<ISeal> seals = new ArrayList<ISeal>();
        seals.addAll(passiveSeals);
        seals.addAll(attackSeals);
        return seals;
    }

    public List<ISeal> getAllSeals() {
        List<ISeal> seals = new ArrayList<ISeal>();
        seals.addAll(passiveSeals);
        seals.addAll(attackSeals);
        seals.addAll(amplifyingSeals);
        return seals;
    }

    public List<ISeal> getAllSealsThatAreNotActivated() {
        List<ISeal> seals = new ArrayList<ISeal>();
        seals.addAll(passiveSeals);
        seals.addAll(attackSeals);
        seals.addAll(amplifyingSeals);
        seals.remove(activeSeal);
        return seals;
    }

    public List<ResourceLocation> getAllSealsIdsThatAreNotActivated() {
        List<ResourceLocation> seals = new ArrayList<>();
        createIfNull();
        passiveSeals.forEach((passiveSeal -> {
            seals.add(passiveSeal.getId());
        }));
        attackSeals.forEach((attackSeal -> {
            seals.add(attackSeal.getId());
        }));
        amplifyingSeals.forEach((amplifyingSeal -> {
            seals.add(amplifyingSeal.getId());
        }));
        if (activeSeal != null) {
            seals.remove(activeSeal.getId());
        }
        return seals;
    }

    public List<ISeal> getAttackSeals() {
        createIfNull();
        return attackSeals;
    }

    public Set<ISeal> getUniqueAttackSeals() {
        createIfNull();
        return Set.copyOf(attackSeals);
    }

    public List<ISeal> getAmplifyingSeals() {
        createIfNull();
        return amplifyingSeals;
    }

    public Set<ISeal> getUniqueAmplifyingSeals() {
        createIfNull();
        return Set.copyOf(amplifyingSeals);
    }

    public void addPassiveSeal(ResourceLocation id) {
        createIfNull();
        Set<ISeal> toRemove = new HashSet<>();
        passiveSeals.forEach((passive -> {
            if (passive instanceof Tiered tiered && SealRegistry.passiveSeals.get(id) instanceof Tiered tiered1 && !tiered.getTierId().equals(tiered1.getTierId())) {
                toRemove.add(passive);
            }
        }));
        passiveSeals.removeAll(toRemove);
        passiveSeals.add(SealRegistry.passiveSeals.get(id));
    }

    public void addAttackSeal(ResourceLocation id) {
        createIfNull();
        Set<ISeal> toRemove = new HashSet<>();
        attackSeals.forEach((attack -> {
            if (attack instanceof Tiered tiered && SealRegistry.attackSeals.get(id) instanceof Tiered tiered1 && !tiered.getTierId().equals(tiered1.getTierId())) {
                toRemove.add(attack);
            }
        }));
        attackSeals.removeAll(toRemove);
        attackSeals.add(SealRegistry.attackSeals.get(id));
    }

    public void addAmplifyingSeal(ResourceLocation id) {
        createIfNull();
        Set<ISeal> toRemove = new HashSet<>();
        amplifyingSeals.forEach((amplifying -> {
            if (amplifying instanceof Tiered tiered && SealRegistry.amplifyingSeals.get(id) instanceof Tiered tiered1 && !tiered.getTierId().equals(tiered1.getTierId())) {
                toRemove.add(amplifying);
            }
        }));
        amplifyingSeals.removeAll(toRemove);
        amplifyingSeals.add(SealRegistry.amplifyingSeals.get(id));
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

    public int getAmountOfTimesThatSealIsPresent(SealType type, ISeal seal) {
        return switch (type) {
            case PASSIVE -> Collections.frequency(passiveSeals, seal);
            case OFFENSIVE -> Collections.frequency(attackSeals, seal);
            case AMPLIFYING -> Collections.frequency(amplifyingSeals, seal);
        };
    }

    public int getAmountOfTimesThatSealIsPresent(SealType type, ResourceLocation sealId) {
        createIfNull();
        return switch (type) {
            case PASSIVE -> Collections.frequency(passiveSeals, SealRegistry.passiveSeals.get(sealId));
            case OFFENSIVE -> Collections.frequency(attackSeals, SealRegistry.attackSeals.get(sealId));
            case AMPLIFYING -> Collections.frequency(amplifyingSeals, SealRegistry.amplifyingSeals.get(sealId));
        };
    }

    public void copyFrom(WandSealAttachment source) {
        this.passiveSeals = source.passiveSeals;
        this.attackSeals = source.attackSeals;
        this.amplifyingSeals = source.amplifyingSeals;
        this.selectedIsAttack = source.selectedIsAttack;
        this.selectedSealSlot = source.selectedSealSlot;
        this.activeSeal = source.activeSeal;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (this.passiveSeals != null && this.attackSeals != null && this.amplifyingSeals != null) {
            for (ResourceLocation sealId : SealRegistry.passiveSeals.keySet()) {
                nbt.putInt(sealId.toString(), Collections.frequency(passiveSeals, SealRegistry.passiveSeals.get(sealId)));
            }
            for (ResourceLocation sealId : SealRegistry.attackSeals.keySet()) {
                nbt.putInt(sealId.toString(), Collections.frequency(attackSeals, SealRegistry.attackSeals.get(sealId)));
            }
            for (ResourceLocation sealId : SealRegistry.amplifyingSeals.keySet()) {
                nbt.putInt(sealId.toString(), Collections.frequency(amplifyingSeals, SealRegistry.amplifyingSeals.get(sealId)));
            }
            if (activeSeal != null) {
                nbt.putString("active_seal", activeSeal.getId().toString());
            }
            nbt.putString("customModel", customWandModel == null ? "": customWandModel.toString());
            nbt.putInt("active_slot", selectedSealSlot);
            nbt.putBoolean("active_slot_attack", selectedIsAttack);
            saveCustomActiveSealModels(nbt, activeSealModels == null? new HashMap<>(): activeSealModels);
        }
        return nbt;
    }

    public void saveCustomActiveSealModels(CompoundTag nbt, Map<ResourceLocation, ResourceLocation> activeSealModels) {
        CompoundTag modelsTag = new CompoundTag();
        activeSealModels.forEach((seal, name) -> {
            if (!modelsTag.contains(seal.toString())) {
                modelsTag.putString(seal.toString(), name.toString());
            }
        });
        nbt.put("activeModels", modelsTag);
    }

    public Map<ResourceLocation, ResourceLocation> deserializeCustomActiveSealModels(CompoundTag nbt) {
        Map<ResourceLocation, ResourceLocation> models = new HashMap<>();
        if (nbt.contains("activeModels")) {
            CompoundTag modelsTag = nbt.getCompound("activeModels");
            for (String key : modelsTag.getAllKeys()) {
               models.put(new ResourceLocation(key), new ResourceLocation(modelsTag.getString(key)));
            }
        }
        return models;
    }

    public void deserializeNBT(CompoundTag nbt) {
        createIfNull();
        for (ResourceLocation sealId : SealRegistry.passiveSeals.keySet()) {
            if (nbt.getInt(sealId.toString()) > 0) {
                for (int i = 1; i <= nbt.getInt(sealId.toString()); ++i) {
                    passiveSeals.add(SealRegistry.passiveSeals.get(sealId));
                }
            }
        }
        for (ResourceLocation sealId : SealRegistry.attackSeals.keySet()) {
            if (nbt.getInt(sealId.toString()) > 0) {
                for (int i = 1; i <= nbt.getInt(sealId.toString()); ++i) {
                    attackSeals.add(SealRegistry.attackSeals.get(sealId));
                }
            }
        }
        for (ResourceLocation sealId : SealRegistry.amplifyingSeals.keySet()) {
            if (nbt.getInt(sealId.toString()) > 0) {
                for (int i = 1; i <= nbt.getInt(sealId.toString()); ++i) {
                    amplifyingSeals.add(SealRegistry.amplifyingSeals.get(sealId));
                }
            }
        }
        activeSeal = SealRegistry.allSeals.get(new ResourceLocation(nbt.getString("active_seal")));
        selectedSealSlot = nbt.getInt("active_slot");
        selectedIsAttack = nbt.getBoolean("active_slot_attack");
        customWandModel = new ResourceLocation(nbt.getString("customModel"));
        activeSealModels = deserializeCustomActiveSealModels(nbt);
    }

    public boolean hasSeal() {
        createIfNull();
        return !passiveSeals.isEmpty() || !attackSeals.isEmpty() || !amplifyingSeals.isEmpty();
    }

    public ResourceLocation getCustomWandModel() {
        return customWandModel;
    }

    public void setCustomWandModel(ResourceLocation customWandModel, ItemStack stack) {
        this.customWandModel = customWandModel;
        CompoundTag tag = stack.getTag().copy();
        tag.putString("customModel", hasCustomWandModel() ? customWandModel.toString(): "");
        stack.setTag(tag);
    }

    public boolean hasCustomWandModel() {
        return customWandModel != null && customWandModel != MissingTextureAtlasSprite.getLocation();
    }

    public boolean hasCustomActiveSealModel(ResourceLocation activeSeal) {
        return activeSealModels != null && activeSealModels.containsKey(activeSeal);
    }

    public Map<ResourceLocation, ResourceLocation> getActiveSealModels() {
        return activeSealModels;
    }

    public void setActiveSealModels(Map<ResourceLocation, ResourceLocation> activeSealModels, ItemStack stack) {
        this.activeSealModels = activeSealModels;
        CompoundTag tag = stack.getTag().copy();
        saveCustomActiveSealModels(tag, activeSealModels == null ? new HashMap<>(): activeSealModels);
        stack.setTag(tag);
    }

    public void addActiveSealModel(ResourceLocation seal, ResourceLocation model, ItemStack stack) {
        if (activeSealModels == null) {
            activeSealModels = new HashMap<>();
        }
        this.activeSealModels.put(seal, model);
        CompoundTag tag = stack.getTag().copy();
        saveCustomActiveSealModels(tag, activeSealModels == null ? new HashMap<>(): activeSealModels);
        stack.setTag(tag);
    }

    public static boolean areCompatible(WandSealAttachment first, WandSealAttachment second) {
        return first.equals(second);
    }
}