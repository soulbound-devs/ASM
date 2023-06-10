package net.vakror.asm.packets;

import dustw.libgui.network.LibGuiPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vakror.asm.ASMMod;

public class ModPackets {
    public static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ASMMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncSoulS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSoulS2CPacket::new)
                .encoder(SyncSoulS2CPacket::encode)
                .consumerNetworkThread(SyncSoulS2CPacket::handle)
                .add();

        net.messageBuilder(SoulFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SoulFluidSyncS2CPacket::new)
                .encoder(SoulFluidSyncS2CPacket::encode)
                .consumerMainThread(SoulFluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(PacketSyncDimensionListChanges.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncDimensionListChanges::new)
                .encoder(PacketSyncDimensionListChanges::encode)
                .consumerMainThread(PacketSyncDimensionListChanges::handle)
                .add();

        net.messageBuilder(LibGuiPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LibGuiPacket::new)
                .encoder(LibGuiPacket::toBytes)
                .consumerMainThread(LibGuiPacket::handler)
                .add();

        net.messageBuilder(SyncSackC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SyncSackC2SPacket::new)
                .encoder(SyncSackC2SPacket::toBytes)
                .consumerMainThread(SyncSackC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}