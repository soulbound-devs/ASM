package net.vakror.asm.capability.wand;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemSealProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<ItemSeal> SEAL = CapabilityManager.get(new CapabilityToken<ItemSeal>() { });

    private ItemSeal itemSeal = null;
    private final LazyOptional<ItemSeal> optional = LazyOptional.of(this::createSeal);

    private @NotNull ItemSeal createSeal() {
        if (this.itemSeal == null) {
            this.itemSeal = new ItemSeal();
        }

        return this.itemSeal;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SEAL) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createSeal().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createSeal().loadNBTData(nbt);
    }
}