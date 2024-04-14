package net.vakror.soulbound.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class ModPackets {
    public static <MSG extends CustomPacketPayload> void sendToServer(MSG packet) {
        PacketDistributor.SERVER.noArg().send(packet);
    }

    public static <MSG extends CustomPacketPayload> void sendToClient(MSG packet, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(packet);
    }

    public static <MSG extends CustomPacketPayload> void sendToClients(MSG message) {
        PacketDistributor.ALL.noArg().send(message);
    }
}