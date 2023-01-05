package net.vakror.soulbound.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.screen.WandImbuingMenu;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.wand.ItemWandProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WandImbuingTableBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 108;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int burningTime;

    public WandImbuingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.WAND_IMBUING_TABLE_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> WandImbuingTableBlockEntity.this.progress;
                    case 1 -> WandImbuingTableBlockEntity.this.maxProgress;
                    case 2 -> WandImbuingTableBlockEntity.this.burningTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> WandImbuingTableBlockEntity.this.progress = pValue;
                    case 1 -> WandImbuingTableBlockEntity.this.maxProgress = pValue;
                    case 2 -> WandImbuingTableBlockEntity.this.burningTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Wand Imbuing Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WandImbuingMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("progress", progress);
        pTag.putInt("burning_time", burningTime);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("progress");
        burningTime = pTag.getInt("burning_time");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, WandImbuingTableBlockEntity pBlockEntity) {
        if (hasRecipe(pBlockEntity) && hasNotReachedStackLimit(pBlockEntity)) {
            pBlockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if (pBlockEntity.progress >= pBlockEntity.maxProgress) {
                craftItem(pBlockEntity);
                setChanged(pLevel, pPos, pState);
            }
        }
        else {
            pBlockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
        if (pBlockEntity.itemHandler.getStackInSlot(0).getItem().equals(ModItems.SOUL.get()) && pBlockEntity.burningTime <= 0 && hasRecipe(pBlockEntity)) {
            pBlockEntity.itemHandler.getStackInSlot(0).shrink(1);
            pBlockEntity.burningTime = 200;
            setChanged(pLevel, pPos, pState);
        }
        if (hasEnoughSoulLeft(pBlockEntity)) {
            pBlockEntity.burningTime--;
            setChanged(pLevel, pPos, pState);
        }
    }

    private static boolean hasEnoughSoulLeft(WandImbuingTableBlockEntity pBlockEntity) {
        return pBlockEntity.burningTime > 0;
    }

    private static void craftItem(WandImbuingTableBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        entity.itemHandler.setStackInSlot(3, entity.itemHandler.getStackInSlot(1));
        entity.itemHandler.getStackInSlot(3).getCapability(ItemWandProvider.WAND).ifPresent(wand -> entity.itemHandler.getStackInSlot(1).getCapability(ItemWandProvider.WAND).ifPresent(oldWand -> {
            wand.copyFrom(oldWand);
            if (SealRegistry.passiveSeals.containsKey(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId())) {
                wand.addPassiveSeal(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
            }
            if (SealRegistry.attackSeals.containsKey(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId())) {
                wand.addAttackSeal(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
            }
            if (SealRegistry.amplifyingSeals.containsKey(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId())) {
                wand.addAmplifyingSeal(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
            }
        }));
        entity.itemHandler.extractItem(1, 1, false);
        entity.itemHandler.extractItem(2, 1, false);
        entity.resetProgress();
    }

    private static boolean hasRecipe(WandImbuingTableBlockEntity entity) {
        ItemStack wandStack = entity.itemHandler.getStackInSlot(1);
        ItemStack sealStack = entity.itemHandler.getStackInSlot(2);
        if (wandStack.isEmpty() || sealStack.isEmpty()) {
            return false;
        }
        Item wandItem = wandStack.getItem();
        if (!(wandItem instanceof WandItem)) {
            return false;
        }
        int sealType = ((SealItem) sealStack.getItem()).getType();
        return ((WandItem) wandItem).canAddSeal(wandStack, sealType);
    }


    private static boolean hasNotReachedStackLimit(WandImbuingTableBlockEntity pBlockEntity) {
        return pBlockEntity.itemHandler.getStackInSlot(3).getCount() < pBlockEntity.itemHandler.getStackInSlot(3).getMaxStackSize();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(3).getItem() == output.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).isEmpty();
    }
}
