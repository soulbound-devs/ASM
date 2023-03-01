package net.vakror.soulbound.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.packets.SoulFluidSyncS2CPacket;
import net.vakror.soulbound.screen.SoulSolidifierMenu;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.soul.ModSoul;
import net.vakror.soulbound.wand.ItemWandProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulSolidifierBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == ModItems.TUNGSTEN_INGOT.get();
                case 1 -> false;
                case 2, 3 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                default -> super.isItemValid(slot, stack);
            };
        }
    };
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 61;
    // For upgrades that will be added in the future
    private int amountOfFluidToExtractForSoul = 500;


    private final FluidTank FLUID_TANK = new FluidTank(32000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            assert level != null;
            if (!level.isClientSide) {
                ModPackets.sendToClients(new SoulFluidSyncS2CPacket(this.fluid, worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == ModSoul.SOURCE_SOUL.get() || stack.getFluid() == ModSoul.SOURCE_DARK_SOUL.get();
        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public SoulSolidifierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUL_SOLIDIFIER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SoulSolidifierBlockEntity.this.progress;
                    case 1 -> SoulSolidifierBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SoulSolidifierBlockEntity.this.progress = pValue;
                    case 1 -> SoulSolidifierBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Soul Solidifier");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SoulSolidifierMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("progress", progress);
        nbt = FLUID_TANK.writeToNBT(nbt);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("progress");
        FLUID_TANK.readFromNBT(pTag);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, SoulSolidifierBlockEntity blockEntity) {
        if (hasNotReachedStackLimit(blockEntity) && hasEnoughFluid(blockEntity)) {
            blockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if (blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(blockEntity);
                setChanged(pLevel, pPos, pState);
            }
        }
        else {
            blockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
        if (hasFluidItemInSourceSlot(blockEntity)) {
            transferItemFluidToFluidTank(blockEntity);
        }
    }

    private static boolean hasEnoughFluid(SoulSolidifierBlockEntity blockEntity) {
        return blockEntity.FLUID_TANK.getFluidAmount() >= blockEntity.amountOfFluidToExtractForSoul;
    }

    private static void transferItemFluidToFluidTank(SoulSolidifierBlockEntity blockEntity) {
        blockEntity.itemHandler.getStackInSlot(3).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
            int drainAmount = Math.min(blockEntity.FLUID_TANK.getSpace(), 1000);

            FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
            if (blockEntity.FLUID_TANK.isFluidValid(stack) && stack.getFluid().isSame(ModSoul.SOURCE_SOUL.get()) || stack.getFluid().isSame(ModSoul.SOURCE_DARK_SOUL.get())) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(blockEntity, stack, handler.getContainer());
            }
        });
    }

    private static void fillTankWithFluid(SoulSolidifierBlockEntity blockEntity, FluidStack stack, ItemStack container) {
        blockEntity.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);

        blockEntity.itemHandler.extractItem(0, 1, false);
        blockEntity.itemHandler.insertItem(0, container, false);
    }

    private static boolean hasFluidItemInSourceSlot(SoulSolidifierBlockEntity blockEntity) {
        return blockEntity.itemHandler.getStackInSlot(3).getCount() > 0;
    }

    private static void craftItem(SoulSolidifierBlockEntity entity) {
        int numberOfSoulToAdd = 1;
        entity.FLUID_TANK.drain(entity.amountOfFluidToExtractForSoul, IFluidHandler.FluidAction.EXECUTE);
        entity.itemHandler.insertItem(1, new ItemStack(entity.itemHandler.getStackInSlot(1).getItem(), entity.itemHandler.getStackInSlot(1).getCount() + numberOfSoulToAdd), false);
        entity.resetProgress();
    }


    private static boolean hasNotReachedStackLimit(SoulSolidifierBlockEntity pBlockEntity) {
        return pBlockEntity.itemHandler.getStackInSlot(3).getCount() < pBlockEntity.itemHandler.getStackInSlot(3).getMaxStackSize();
    }

    private void resetProgress() {
        this.progress = 0;
    }
}
