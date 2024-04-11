package net.vakror.soulbound.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.client.ClientSoulData;


public class SyncSoulS2CPacket implements CustomPacketPayload {
    int currentSoulAmount;
    long currentMaxSoulAmount;
    int currentDarkSoulAmount;
    long currentDarkMaxSoulAmount;

    public SyncSoulS2CPacket(int currentSoulAmount, int currentMaxSoulAmount, int currentDarkSoulAmount, int currentDarkMaxSoulAmount) {
        this.currentSoulAmount = currentSoulAmount;
        this.currentMaxSoulAmount = currentMaxSoulAmount;
        this.currentDarkSoulAmount = currentDarkSoulAmount;
        this.currentDarkMaxSoulAmount = currentDarkMaxSoulAmount;
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

    public boolean handle(PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> ClientSoulData.set(currentSoulAmount, currentDarkSoulAmount, (int) currentMaxSoulAmount, (int) currentDarkMaxSoulAmount));
        return true;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(buf);
    }

    public static final ResourceLocation ID = new ResourceLocation(SoulboundMod.MOD_ID, "sync_soul");

    @Override
    public ResourceLocation id() {
        return ID;
    }
}