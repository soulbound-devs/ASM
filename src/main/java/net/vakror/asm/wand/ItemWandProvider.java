package net.vakror.asm.wand;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemWandProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<ItemWand> WAND = CapabilityManager.get(new CapabilityToken<ItemWand>() { });

    private ItemWand itemWand = null;
    private final LazyOptional<ItemWand> optional = LazyOptional.of(this::createWand);

    private @NotNull ItemWand createWand() {
        if (this.itemWand == null) {
            this.itemWand = new ItemWand();
        }

        return this.itemWand;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == WAND) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createWand().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createWand().loadNBTData(nbt);
    }
}