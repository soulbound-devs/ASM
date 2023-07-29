package net.vakror.soulbound.screen;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vakror.soulbound.items.custom.SackItem;
import net.vakror.soulbound.screen.slot.SackSlot;

import java.util.UUID;

public class SackMenu extends AbstractContainerMenu {

    private final SackInventory inv;
    private final Inventory playerInv;
    private final int width;
    public final NonNullList<Slot> playerInvSlots = NonNullList.create();
    private final int height;
    private final int stackLimit;
    private final UUID uuid;

    //gets called clientside. The real inv does not matter
    public SackMenu(int syncId, Inventory playerInventory, FriendlyByteBuf packetByteBuf) {
        this(syncId, playerInventory, packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readInt(), packetByteBuf.readUUID(), Items.AIR.getDefaultInstance());
    }

    public SackMenu(int syncId, Inventory playerInv, int width, int height, int stackLimit, UUID uuid, ItemStack stack) {
        super(ModMenuTypes.SACK_MENU.get(), syncId);
        this.inv = new SackInventory(stack, width * height, stackLimit);
        this.playerInv = playerInv;
        this.width = width;
        this.height = height;
        this.stackLimit = stackLimit;
        this.uuid = uuid;
        // Backpack inventory
        for (int n = 0; n < height; ++n) {
            for (int m = 0; m < width; ++m) {
                this.addSlot(new SackSlot(inv, m + n * width, 8 + m * 18, 18 + n * 18, stackLimit), true);
            }
        }

        // Player inventory
        for (int n = 0; n < 3; ++n) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInv, m + n * 9 + 9, 8 + (width * 18 - 162) / 2 + m * 18, 31 + (height + n) * 18), false);
            }
        }

        // Player hotbar
        for (int n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInv, n, 8 + (width * 18 - 162) / 2 + n * 18, 89 + height * 18), false);
        }

        this.inv.startOpen(playerInv.player);
    }

    @Override
    public boolean stillValid(Player player) {
        if (player.level.isClientSide) return true;

        var stack = inv.getHolderStack();
        var uuidMatch = SackItem.isUUIDMatch(stack, this.uuid);

        return !stack.isEmpty() && stack.getItem() instanceof SackItem && uuidMatch;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var stack = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            final var stack2 = slot.getItem();
            stack = stack2.copy();

            if (index < this.inv.size) {
                if (!this.moveItemStackTo(stack2, this.inv.size, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, this.inv.size, false)) {
                return ItemStack.EMPTY;
            }

            if (stack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }

    @Override
    public void clicked(int slotIndex, int button, ClickType type, Player player) {
        if (slotIndex >= 0 && player.getInventory().selected + 27 + this.inv.size == slotIndex) {
            if (type != ClickType.CLONE) {
                return;
            }
        }
        super.clicked(slotIndex, button, type, player);
    }

    public Slot addSlot(Slot slot, boolean isSack) {
        if (isSack) {
            return this.addSlot(slot);
        }
        playerInvSlots.add(slot);
        return this.addSlot(slot);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inv.stopOpen(player);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while(!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = stack.getMaxStackSize() == 1 ? 1: slot.getMaxStackSize();
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.setChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while(true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.set(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.set(stack.split(stack.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }
}