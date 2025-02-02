package net.vakror.soulbound.block.entity.custom;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.vakror.soulbound.block.entity.ModBlockEntities;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.packets.ModPackets;
import net.vakror.soulbound.packets.SoulFluidSyncS2CPacket;
import net.vakror.soulbound.screen.SoulSolidifierMenu;
import net.vakror.soulbound.soul.ModSoul;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO: cache fluid cap
public class SoulSolidifierBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == ModItems.TUNGSTEN_INGOT.get();
                case 1 -> true;
                case 2, 3 -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
                default -> super.isItemValid(slot, stack);
            };
        }
    };
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 156;
    // For upgrades that will be added in the future
    private final int amountOfFluidToExtractForSoul = 100;


    public final FluidTank FLUID_TANK = new FluidTank(1600) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModPackets.sendToClients(new SoulFluidSyncS2CPacket(this.getFluid(), worldPosition));
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

    public FluidTank getFluidTank(Direction direction) {
        return FLUID_TANK;
    }

    public ItemStackHandler getItemHandler(Direction direction) {
        return itemHandler;
    }

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
        ModPackets.sendToClients(new SoulFluidSyncS2CPacket(this.getFluidStack(), worldPosition));
        return new SoulSolidifierMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
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

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, SoulSolidifierBlockEntity blockEntity) {
        for (Direction direction : Direction.values()) {
            blockEntity.pullFluidFromInv(pLevel, pPos.relative(direction), direction);
        }

        if (!pLevel.isClientSide && hasNotReachedStackLimit(blockEntity) && (blockEntity.itemHandler.getStackInSlot(1).isEmpty() || blockEntity.fluidSameAsSoulItem(blockEntity.itemHandler.getStackInSlot(1).getItem())) && hasEnoughFluid(blockEntity) && hasTungsten(blockEntity) && (blockEntity.FLUID_TANK.getFluid().getFluid() == ModSoul.SOURCE_DARK_SOUL.get() || blockEntity.FLUID_TANK.getFluid().getFluid() == ModSoul.SOURCE_SOUL.get())) {
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
        if (hasFluidItemInDrainSlot(blockEntity)) {
            transferFluidTankToItem(blockEntity);
        }
    }

    public void pullFluidFromInv(Level level, BlockPos pos, Direction direction) {
        if (level.getBlockEntity(pos) != null) {
            @Nullable IFluidHandler cap = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);
            if (cap != null) {
                for (int i = 0; i < cap.getTanks(); i++) {
                    if (cap.getFluidInTank(i).getFluid().equals(ModSoul.SOURCE_SOUL.get()) || cap.getFluidInTank(i).getFluid().equals(ModSoul.SOURCE_DARK_SOUL.get())) {
                        int amountToDrain = Math.min(1000, cap.getFluidInTank(i).getAmount());
                        if (amountToDrain > FLUID_TANK.getSpace()) {
                            amountToDrain = FLUID_TANK.getSpace();
                        }
                        if (amountToDrain > 0 && FLUID_TANK.isFluidValid(cap.getFluidInTank(i))) {
                            FLUID_TANK.fill(new FluidStack(cap.getFluidInTank(i), amountToDrain), IFluidHandler.FluidAction.EXECUTE);
                            cap.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
                invalidateCapabilities();
            }
        }
    }

    private static void transferFluidTankToItem(SoulSolidifierBlockEntity blockEntity) {
        IFluidHandlerItem handler = blockEntity.itemHandler.getStackInSlot(3).getCapability(Capabilities.FluidHandler.ITEM);
        int drainAmount = Math.min(handler.getTankCapacity(0) - handler.getFluidInTank(0).getAmount(), 1000);

        int amount = handler.fill(new FluidStack(blockEntity.FLUID_TANK.getFluid().getFluid(), drainAmount), IFluidHandler.FluidAction.SIMULATE);
        if (amount > 0) {
            handler.fill(new FluidStack(blockEntity.FLUID_TANK.getFluid().getFluid(), drainAmount), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private static boolean hasFluidItemInDrainSlot(SoulSolidifierBlockEntity blockEntity) {
        return blockEntity.itemHandler.getStackInSlot(3).getCount() > 0;
    }

    private static boolean hasTungsten(SoulSolidifierBlockEntity blockEntity) {
        return blockEntity.itemHandler.getStackInSlot(0).is(ModItems.TUNGSTEN_INGOT.get());
    }

    private static boolean hasEnoughFluid(SoulSolidifierBlockEntity blockEntity) {
        return blockEntity.FLUID_TANK.getFluidAmount() >= blockEntity.amountOfFluidToExtractForSoul;
    }

    public boolean fluidSameAsSoulItem(Item soul) {
        return soul.equals(ModItems.SOUL.get()) ? this.FLUID_TANK.getFluid().getFluid().equals(ModSoul.SOURCE_SOUL.get()): this.FLUID_TANK.getFluid().getFluid().equals(ModSoul.SOURCE_DARK_SOUL.get());
    }

    private static void transferItemFluidToFluidTank(SoulSolidifierBlockEntity blockEntity) {
        IFluidHandlerItem handler = blockEntity.itemHandler.getStackInSlot(2).getCapability(Capabilities.FluidHandler.ITEM);
        int drainAmount = Math.min(blockEntity.FLUID_TANK.getSpace(), 1000);

        FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
        if (blockEntity.FLUID_TANK.isFluidValid(stack)) {
            if (blockEntity.FLUID_TANK.getFluid().getFluid().isSame(stack.getFluid()) || blockEntity.FLUID_TANK.isEmpty()) {
                stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);
                fillTankWithFluid(blockEntity, stack, handler.getContainer());
            }
        }
    }

    private static void fillTankWithFluid(SoulSolidifierBlockEntity blockEntity, FluidStack stack, ItemStack container) {
        if (stack.getFluid().equals(ModSoul.SOURCE_SOUL.get()) || stack.getFluid().equals(ModSoul.SOURCE_DARK_SOUL.get())) {
            blockEntity.FLUID_TANK.fill(stack, IFluidHandler.FluidAction.EXECUTE);

            blockEntity.itemHandler.extractItem(2, 1, false);
            blockEntity.itemHandler.insertItem(2, container, false);
        }
    }

    private static boolean hasFluidItemInSourceSlot(SoulSolidifierBlockEntity blockEntity) {
        return blockEntity.itemHandler.getStackInSlot(2).getCount() > 0;
    }

    private static void craftItem(SoulSolidifierBlockEntity entity) {
        int numberOfSoulToAdd = 1;
        Item typeOfItemToAdd = entity.FLUID_TANK.getFluid().getFluid().equals(ModSoul.SOURCE_SOUL.get()) ? ModItems.SOUL.get(): ModItems.DARK_SOUL.get();
        entity.FLUID_TANK.drain(entity.amountOfFluidToExtractForSoul, IFluidHandler.FluidAction.EXECUTE);
        entity.itemHandler.extractItem(0, 1, false);
        ItemStack result;
        if (entity.itemHandler.getStackInSlot(1).isEmpty()) {
             result = new ItemStack(typeOfItemToAdd, numberOfSoulToAdd);
        } else {
            result = new ItemStack(entity.itemHandler.getStackInSlot(1).getItem(), 1);
        }
        entity.itemHandler.insertItem(1, result, false);
        entity.resetProgress();
    }


    private static boolean hasNotReachedStackLimit(SoulSolidifierBlockEntity pBlockEntity) {
        return pBlockEntity.itemHandler.getStackInSlot(1).getCount() < pBlockEntity.itemHandler.getStackInSlot(1).getMaxStackSize();
    }

    private void resetProgress() {
        this.progress = 0;
    }
}
