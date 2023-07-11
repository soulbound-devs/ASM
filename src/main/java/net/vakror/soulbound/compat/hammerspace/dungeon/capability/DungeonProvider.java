package net.vakror.soulbound.compat.hammerspace.dungeon.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DungeonProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<Dungeon> DUNGEON = CapabilityManager.get(new CapabilityToken<Dungeon>() { });

    private Dungeon dungeon = null;
    private final LazyOptional<Dungeon> optional = LazyOptional.of(this::createSeal);

    private @NotNull Dungeon createSeal() {
        if (this.dungeon == null) {
            this.dungeon = new Dungeon();
        }

        return this.dungeon;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == DUNGEON) {
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