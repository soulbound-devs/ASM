package net.vakror.soulbound.wand;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemWandProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<Wand> WAND = CapabilityManager.get(new CapabilityToken<Wand>() { });

    private Wand wand = null;
    private final LazyOptional<Wand> optional = LazyOptional.of(this::createWand);

    private @NotNull Wand createWand() {
        if (this.wand == null) {
            this.wand = new Wand();
        }

        return this.wand;
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