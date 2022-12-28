package net.vakror.unmm.networking;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.datafix.fixes.PlayerUUIDFix;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.unmm.client.ClientSoulData;
import net.vakror.unmm.soul.PlayerSoulProvider;

public class SyncSoulS2CPacket {
    int currentSoulAmount;
    long currentMaxSoulAmount;
    int currentDarkSoulAmount;
    long currentDarkMaxSoulAmount;

    public SyncSoulS2CPacket(int currentSoulAmount, int currentMaxSoulAmount, int currentDarkSoulAmount, int currentDarkMaxSoulAmount) {
        this.currentSoulAmount = currentSoulAmount;
        this.currentMaxSoulAmount = currentMaxSoulAmount;
        this.currentDarkSoulAmount = currentSoulAmount;
        this.currentDarkMaxSoulAmount = currentMaxSoulAmount;
    }

    public SyncSoulS2CPacket(FriendlyByteBuf buffer) {
        this.currentSoulAmount = buffer.readInt();
        this.currentMaxSoulAmount = buffer.readLong();
        this.currentDarkSoulAmount = buffer.readInt();
        this.currentDarkMaxSoulAmount = buffer.readLong();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(currentSoulAmount);
        buffer.writeLong(currentMaxSoulAmount);
        buffer.writeInt(currentDarkSoulAmount);
        buffer.writeLong(currentDarkMaxSoulAmount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientSoulData.set(currentSoulAmount);
            ClientSoulData.setMax(currentMaxSoulAmount);
        });
        return true;
    }
}