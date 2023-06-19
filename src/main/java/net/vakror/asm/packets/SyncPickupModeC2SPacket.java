package net.vakror.asm.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.asm.items.custom.SackItem;
import net.vakror.asm.util.PickupUtil;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncPickupModeC2SPacket {

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

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = supplier.get().getSender();

            if (player != null) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SackItem sackItem && sackItem.canPickup(player.getItemInHand(InteractionHand.MAIN_HAND))) {
                    sackItem.setPickupMode(PickupUtil.PickupMode.next(sackItem.getPickupMode()));

                    supplier.get().getNetworkManager().send(new ClientboundSetActionBarTextPacket(Component.literal("Pickup Mode: " + sackItem.getPickupMode())));
                }
            }
        });
        return true;
    }
}