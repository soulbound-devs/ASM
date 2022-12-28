package net.vakror.unmm.soul;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PlayerSoulProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerSoul> PLAYER_SOUL = CapabilityManager.get(new CapabilityToken<PlayerSoul>() { });

    private PlayerSoul soul = null;
    private final LazyOptional<PlayerSoul> optional = LazyOptional.of(this::createPlayerSoul);

    private @NotNull PlayerSoul createPlayerSoul() {
        if (this.soul == null) {
            this.soul = new PlayerSoul();
        }

        return this.soul;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_SOUL) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSoul().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSoul().loadNBTData(nbt);
    }
}















