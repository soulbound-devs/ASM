package net.vakror.soulbound.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.blocks.entity.custom.SoulExtractorBlockEntity;
import net.vakror.soulbound.soul.ModSoul;

import java.util.ArrayList;
import java.util.List;

public class SoulExtractorMenu extends AbstractContainerMenu {
    public final SoulExtractorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private FluidStack soulStack;
    private FluidStack darkSoulStack;

    private final List<Slot> playerInvSlots = new ArrayList<>();
    private final List<Slot> invSlots = new ArrayList<>();

    public SoulExtractorMenu(int pContainerId, Inventory inv, FriendlyByteBuf data) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(data.readBlockPos()), new SimpleContainerData(2));
    }

    public SoulExtractorMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.SOUL_EXTRACTOR_MENU.get(), pContainerId);
        checkContainerSize(inv, 4);
        this.blockEntity = ((SoulExtractorBlockEntity) entity);
        this.level = inv.player.level;
        this.data = data;
        this.soulStack = ((FluidTank) blockEntity.SOUL_HANDLER.orElse(new FluidTank(0))).getFluid();
        this.darkSoulStack = ((FluidTank) blockEntity.DARK_SOUL_HANDLER.orElse(new FluidTank(0))).getFluid();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);
    }


    public List<Slot> getPlayerInvSlots() {
        return playerInvSlots;
    }

    public List<Slot> getInvSlots() {
        return invSlots;
    }

    public SoulExtractorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void setFluid(FluidStack fluidStack) {
        if (fluidStack.getFluid().isSame(ModSoul.SOURCE_SOUL.get())) {
            this.soulStack = fluidStack;
        } else {
            this.darkSoulStack = fluidStack;
        }
    }

    @Override
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }

    public FluidStack getSoulStack() {
        return soulStack;
    }

    public FluidStack getDarkSoulStack() {
        return darkSoulStack;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE FOR CUSTOM BLOCK ENTITIES!!!!!
    private static final int TE_INVENTORY_SLOT_COUNT = 0;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            SoulboundMod.LOGGER.error("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.SOUL_EXTRACTOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                Slot slot = new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18);
                this.addSlot(slot);
                playerInvSlots.add(slot);
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            Slot slot = new Slot(playerInventory, i, 8 + i * 18, 144);
            this.addSlot(slot);
            playerInvSlots.add(slot);
        }
    }
}
