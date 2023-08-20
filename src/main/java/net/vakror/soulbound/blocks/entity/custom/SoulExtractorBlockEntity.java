package net.vakror.soulbound.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.vakror.soulbound.blocks.custom.SoulExtractorBlock;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.packets.SoulFluidSyncS2CPacket;
import net.vakror.soulbound.packets.SyncSoulS2CPacket;
import net.vakror.soulbound.screen.SoulExtractorMenu;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.soul.PlayerSoulProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulExtractorBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    private int amountOfSoulToPullPerTick = 10;
    private int amountOfSoulFluidPerSoul = 10;

    public SoulExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUL_EXTRACTOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SoulExtractorBlockEntity.this.amountOfSoulToPullPerTick;
                    case 1 -> SoulExtractorBlockEntity.this.amountOfSoulFluidPerSoul;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SoulExtractorBlockEntity.this.amountOfSoulToPullPerTick = pValue;
                    case 1 -> SoulExtractorBlockEntity.this.amountOfSoulFluidPerSoul = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        SOUL_HANDLER = LazyOptional.of(() -> SOUL_TANK);
        DARK_SOUL_HANDLER = LazyOptional.of(() -> DARK_SOUL_TANK);
    }

    public final FluidTank SOUL_TANK = new FluidTank(1600) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModPackets.sendToClients(new SoulFluidSyncS2CPacket(getFluid(), worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == ModSoul.SOURCE_SOUL.get();
        }
    };

    public void setFluid(FluidStack stack) {
        if (stack.getFluid().isSame(ModSoul.SOURCE_SOUL.get())) {
            ((FluidTank) SOUL_HANDLER.orElse(new FluidTank(0))).setFluid(stack);
        } else {
            ((FluidTank) DARK_SOUL_HANDLER.orElse(new FluidTank(0))).setFluid(stack);
        }
    }

    public final FluidTank DARK_SOUL_TANK = new FluidTank(1600) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModPackets.sendToClients(new SoulFluidSyncS2CPacket(getFluid(), worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == ModSoul.SOURCE_DARK_SOUL.get();
        }
    };

    public LazyOptional<IFluidHandler> SOUL_HANDLER;
    public LazyOptional<IFluidHandler> DARK_SOUL_HANDLER;

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("amountOfSoulToPullPerTick", amountOfSoulToPullPerTick);
        pTag.putInt("amountOfSoulFluidPerSoul", amountOfSoulFluidPerSoul);
        CompoundTag soulNbt = new CompoundTag();
        CompoundTag darkSoulNbt = new CompoundTag();
        soulNbt = ((FluidTank) SOUL_HANDLER.orElse(new FluidTank(0))).writeToNBT(soulNbt);
        darkSoulNbt = ((FluidTank) DARK_SOUL_HANDLER.orElse(new FluidTank(0))).writeToNBT(darkSoulNbt);
        pTag.put("soul", soulNbt);
        pTag.put("darkSoul", darkSoulNbt);
        super.saveAdditional(pTag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction direction) {
        if (cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
                Direction localDir = this.getBlockState().getValue(SoulExtractorBlock.FACING);
                if (direction != null) {
                    return switch (localDir) {
                        default -> direction.getOpposite() == Direction.EAST ? SOUL_HANDLER.cast() : direction.getOpposite() == Direction.WEST ? DARK_SOUL_HANDLER.cast() : LazyOptional.empty();
                        case EAST -> direction.getClockWise() == Direction.EAST ? SOUL_HANDLER.cast() : direction.getClockWise() == Direction.WEST ? DARK_SOUL_HANDLER.cast() : LazyOptional.empty();
                        case SOUTH -> direction == Direction.EAST ? SOUL_HANDLER.cast() : direction == Direction.WEST ? DARK_SOUL_HANDLER.cast() : LazyOptional.empty();
                        case WEST -> direction.getCounterClockWise() == Direction.EAST ? SOUL_HANDLER.cast() : direction.getCounterClockWise() == Direction.WEST ? DARK_SOUL_HANDLER.cast() : LazyOptional.empty();
                    };
                }
        }
        return super.getCapability(cap, direction);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        amountOfSoulToPullPerTick = pTag.getInt("amountOfSoulToPullPerTick");
        amountOfSoulFluidPerSoul = pTag.getInt("amountOfSoulFluidPerSoul");
        ((FluidTank) SOUL_HANDLER.orElse(new FluidTank(0))).readFromNBT(pTag.getCompound("soul"));
        ((FluidTank) DARK_SOUL_HANDLER.orElse(new FluidTank(0))).readFromNBT(pTag.getCompound("darkSoul"));
    }

    public static void stepOnTick(Level level, BlockPos pos, BlockState state, SoulExtractorBlockEntity entity, Player player) {
        if (!level.isClientSide && state.getBlock() instanceof SoulExtractorBlock) {
            insertSoul(level, pos, state, player, entity);
            setChanged(level, pos, state);
        }
    }

    private static void insertSoul(Level level, BlockPos pos, BlockState state, Player player, SoulExtractorBlockEntity entity) {
        player.getCapability(PlayerSoulProvider.PLAYER_SOUL).ifPresent((soul -> {
            if (soul.getSoul() > 0) {
                int amountOfSoulToPull = Math.max(entity.amountOfSoulToPullPerTick, soul.getSoul());
                if (((FluidTank) entity.SOUL_HANDLER.orElse(new FluidTank(0))).getSpace() >= (amountOfSoulToPull * entity.amountOfSoulFluidPerSoul)) {
                    soul.addSoul(-amountOfSoulToPull);
                    entity.SOUL_HANDLER.orElse(new FluidTank(0)).fill(new FluidStack(ModSoul.SOURCE_SOUL.get(), amountOfSoulToPull * entity.amountOfSoulFluidPerSoul), IFluidHandler.FluidAction.EXECUTE);
                    ModPackets.sendToClient(new SyncSoulS2CPacket(soul.getSoul(), soul.getMaxSoul(), soul.getDarkSoul(), soul.getMaxDarkSoul()), (ServerPlayer) player);
                    ModPackets.sendToClient(new SoulFluidSyncS2CPacket(((FluidTank) entity.SOUL_HANDLER.orElse(new FluidTank(0))).getFluid(), pos), (ServerPlayer) player);
                }
            }
            if (soul.getDarkSoul() > 0) {
                int amountOfSoulToPull = Math.max(entity.amountOfSoulToPullPerTick, soul.getDarkSoul());
                if (((FluidTank) entity.DARK_SOUL_HANDLER.orElse(new FluidTank(0))).getSpace() >= (amountOfSoulToPull * entity.amountOfSoulFluidPerSoul)) {
                    soul.addDarkSoul(-amountOfSoulToPull);
                    entity.DARK_SOUL_HANDLER.orElse(new FluidTank(0)).fill(new FluidStack(ModSoul.SOURCE_DARK_SOUL.get(), amountOfSoulToPull * entity.amountOfSoulFluidPerSoul), IFluidHandler.FluidAction.EXECUTE);
                    ModPackets.sendToClient(new SyncSoulS2CPacket(soul.getSoul(), soul.getMaxSoul(), soul.getDarkSoul(), soul.getMaxDarkSoul()), (ServerPlayer) player);
                    ModPackets.sendToClient(new SoulFluidSyncS2CPacket(((FluidTank) entity.DARK_SOUL_HANDLER.orElse(new FluidTank(0))).getFluid(), pos), (ServerPlayer) player);
                }
            }
        }));
    }

    private int secondsToTicks(int seconds) {
        return seconds * 20;
    }

    public int amountOfSoulToPullPerTick() {
        return amountOfSoulToPullPerTick;
    }

    public SoulExtractorBlockEntity setAmountOfSoulToPullPerTick(int amountOfSoulToPullPerTick) {
        this.amountOfSoulToPullPerTick = amountOfSoulToPullPerTick;
        return this;
    }

    public int amountOfSoulFluidPerSoul() {
        return amountOfSoulFluidPerSoul;
    }

    public SoulExtractorBlockEntity setAmountOfSoulFluidPerSoul(int amountOfSoulFluidPerSoul) {
        this.amountOfSoulFluidPerSoul = amountOfSoulFluidPerSoul;
        return this;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Soul Extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        ModPackets.sendToClients(new SoulFluidSyncS2CPacket(((FluidTank) SOUL_HANDLER.orElse(new FluidTank(0))).getFluid(), worldPosition));
        ModPackets.sendToClients(new SoulFluidSyncS2CPacket(((FluidTank) DARK_SOUL_HANDLER.orElse(new FluidTank(0))).getFluid(), worldPosition));
        return new SoulExtractorMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
