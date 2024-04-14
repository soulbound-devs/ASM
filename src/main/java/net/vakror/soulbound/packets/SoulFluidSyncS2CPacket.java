package net.vakror.soulbound.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.block.entity.custom.SoulExtractorBlockEntity;
import net.vakror.soulbound.block.entity.custom.SoulSolidifierBlockEntity;
import net.vakror.soulbound.screen.SoulExtractorMenu;
import net.vakror.soulbound.screen.SoulSolidifierMenu;

import java.util.function.Supplier;

public class SoulFluidSyncS2CPacket implements CustomPacketPayload {
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
        buf.writeFluidStack(stack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(PlayPayloadContext sup) {
        sup.workHandler().submitAsync(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof SoulSolidifierBlockEntity blockEntity) {
                blockEntity.setFluid(stack);

                if (Minecraft.getInstance().player.containerMenu instanceof SoulSolidifierMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluid(stack);
                }
            }
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof SoulExtractorBlockEntity blockEntity) {
                blockEntity.setFluid(stack);

                if (Minecraft.getInstance().player.containerMenu instanceof SoulExtractorMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluid(stack);
                }
            }
        });
        return true;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(buf);
    }

    public static final ResourceLocation ID = new ResourceLocation(SoulboundMod.MOD_ID, "soul_fluid_sync");

    @Override
    public ResourceLocation id() {
        return ID;
    }
}