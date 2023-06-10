package net.vakror.asm.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSackC2SPacket {

    private final int invIndex;

    public SyncSackC2SPacket(int invIndex) {
        this.invIndex = invIndex;
    }

    public SyncSackC2SPacket(FriendlyByteBuf buf) {
        invIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(invIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            assert player != null;
            ItemStack stack = player.getInventory().getItem(invIndex);
            CompoundTag nbt = (CompoundTag) stack.getTag().get("Inventory");
            nbt.put("Inventory", nbt);
            stack.setTag(nbt);
            player.inventory.setItem(invIndex, stack);
        });
        return true;
    }
}