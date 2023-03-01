package net.vakror.soulbound.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;
import net.vakror.soulbound.blocks.entity.custom.SoulSolidifierBlockEntity;
import net.vakror.soulbound.screen.SoulSolidifierMenu;

import java.util.function.Supplier;

public class SoulFluidSyncS2CPacket {
    private final FluidStack stack;
    private final BlockPos pos;

    public SoulFluidSyncS2CPacket(FluidStack stack, BlockPos pos) {
        this.stack = stack;
        this.pos = pos;
    }

    public SoulFluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.stack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context context = sup.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof SoulSolidifierBlockEntity blockEntity) {
                blockEntity.setFluid(stack);

                if (Minecraft.getInstance().player.containerMenu instanceof SoulSolidifierMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluid(stack);
                }
            }
        });
        return true;
    }
}