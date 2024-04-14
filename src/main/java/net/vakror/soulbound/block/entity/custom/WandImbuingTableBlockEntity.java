package net.vakror.soulbound.block.entity.custom;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.vakror.soulbound.block.entity.ModBlockEntities;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.cap.WandSealAttachment;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.items.custom.SealableItem;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.screen.WandImbuingMenu;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.SealType;
import org.jetbrains.annotations.Nullable;

public class WandImbuingTableBlockEntity extends BlockEntity implements ExtendedMenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 108;

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
        return Component.literal("Wand Imbuing Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WandImbuingMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
        if (hasEnoughSoulLeft(pBlockEntity)) {
            pBlockEntity.burningTime--;
            setChanged(pLevel, pPos, pState);
        }
        if (pBlockEntity.itemHandler.getStackInSlot(0).getItem().equals(ModItems.SOUL.get()) && pBlockEntity.burningTime <= 0 && hasRecipe(pBlockEntity) && pBlockEntity.itemHandler.getStackInSlot(3).isEmpty()) {
            pBlockEntity.itemHandler.getStackInSlot(0).shrink(1);
            pBlockEntity.burningTime = 200;
            setChanged(pLevel, pPos, pState);
        }
        if (hasRecipe(pBlockEntity) && hasNotReachedStackLimit(pBlockEntity) && hasEnoughSoulLeft(pBlockEntity) && pBlockEntity.itemHandler.getStackInSlot(3).isEmpty()) {
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
        WandSealAttachment wand = entity.itemHandler.getStackInSlot(3).getData(ModAttachments.SEAL_ATTACHMENT);
        WandSealAttachment oldWand = entity.itemHandler.getStackInSlot(1).getData(ModAttachments.SEAL_ATTACHMENT);
        wand.copyFrom(oldWand);
        if (SealRegistry.passiveSeals.containsKey(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId())) {
            wand.addPassiveSeal(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
        }
        if (SealRegistry.attackSeals.containsKey(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId())) {
            wand.addAttackSeal(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
        }
        if (SealRegistry.amplifyingSeals.containsKey(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId())) {
            wand.addAmplifyingSeal(((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
        } else {
            System.err.println("SEAL ID NOT FOUND: " + ((SealItem) entity.itemHandler.getStackInSlot(2).getItem()).getId());
            System.err.println("Known Ids: " + String.join(", ", SealRegistry.allSeals.keySet()));
        }
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
        Item sealableItem = wandStack.getItem();
        if (!(sealableItem instanceof SealableItem)) {
            return false;
        }
        SealType sealType = ((SealItem) sealStack.getItem()).getType();
        return ((SealableItem) sealableItem).canAddSeal(wandStack, sealType, sealStack);
    }


    private static boolean hasNotReachedStackLimit(WandImbuingTableBlockEntity pBlockEntity) {
        return pBlockEntity.itemHandler.getStackInSlot(3).getCount() < pBlockEntity.itemHandler.getStackInSlot(3).getMaxStackSize();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public ItemStackHandler getItemHandler(Direction pDirection) {
        return itemHandler;
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }
}
