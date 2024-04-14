package net.vakror.soulbound.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.SackItem;
import net.vakror.soulbound.util.PickupUtil;

import java.util.UUID;

public class SyncPickupModeC2SPacket implements CustomPacketPayload {

    private final UUID playerUUID;

    public SyncPickupModeC2SPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public SyncPickupModeC2SPacket(FriendlyByteBuf buf) {
        playerUUID = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
    }

    public boolean handle(PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> context.player().ifPresent(player -> {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SackItem sackItem && sackItem.canPickup(player.getItemInHand(InteractionHand.MAIN_HAND))) {
                sackItem.setPickupMode(PickupUtil.PickupMode.next(sackItem.getPickupMode()));

                ((ServerPlayer) player).connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Pickup Mode: " + sackItem.getPickupMode())));
            }
        }));
        return true;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(buf);
    }

    public static final ResourceLocation ID = new ResourceLocation(SoulboundMod.MOD_ID, "set_pickup_mode");

    @Override
    public ResourceLocation id() {
        return ID;
    }
}