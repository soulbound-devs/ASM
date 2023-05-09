package net.vakror.asm.backpack.screen;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.vakror.asm.ASMMod;
import net.vakror.asm.screen.ModMenuTypes;
import org.cyclops.cyclopscore.inventory.LargeInventory;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackpackMenu extends ScrollingInventoryContainer<Slot> {
    public int ROWS = 3;
    public int COLUMNS = 9;
    private final List<Slot> slots;
    private final NonNullList<ItemStack> stacks;
    private int lastState = -2;
    private boolean firstDetection = false;
    private List<ContainerListener> listeners = new ArrayList<>();

    public BackpackMenu(int pContainerId, Inventory inv, FriendlyByteBuf data) {
        this(pContainerId, inv, data.readItem(), new LargeInventory(data.readInt(), 64));
    }

    public BackpackMenu(int id, Inventory playerInv, ItemStack wand, Container inventory) {
        super(ModMenuTypes.BACKPACK_MENU.get(), id, playerInv, inventory, Collections.<Slot>emptyList(), (item, pattern) -> true);
        checkContainerSize(playerInv, 27);

        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);

        this.slots = Lists.newArrayListWithCapacity(getSizeInventory());
        this.stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

        this.addSlots(getSizeInventory() / COLUMNS, COLUMNS);
    }

    private void addSlots(int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Slot slot = createSlot(inventory, column + row * columns, 9 + column * 18, 18 + row * 18);
                addSlot(slot);
                slots.add(slot);
            }
        }
    }

    protected void disableSlot(int slotIndex) {
        Slot slot = getSlot(slotIndex);
        // Yes I know this is ugly.
        // If you are reading this and know a better way, please tell me.
        setSlotPosX(slot, Integer.MIN_VALUE);
        setSlotPosY(slot, Integer.MIN_VALUE);
    }

    protected void enableSlot(int slotIndex, int row, int column) {
        Slot slot = getSlot(slotIndex);
        // Yes I know this is ugly.
        // If you are reading this and know a better way, please tell me.
        setSlotPosX(slot, 9 + column * 18);
        setSlotPosY(slot, 18 + row * 18);
    }

    @Override
    public void onScroll(int firstRow) {
        for(int i = 0; i < getUnfilteredItemCount(); i++) {
            disableSlot(i);
        }
        super.onScroll(firstRow);
    }

    @Override
    protected void enableElementAt(int visibleIndex, int elementIndex, Slot element) {
        super.enableElementAt(visibleIndex, elementIndex, element);
        int column = visibleIndex % getColumns();
        int row = (visibleIndex - column) / getColumns();
        enableSlot(elementIndex, row, column);
    }

    @Override
    protected List<Slot> getUnfilteredItems() {
        return this.slots;
    }

    @Override
    public void broadcastChanges() {
        int newState = ((SimpleInventory) inventory).getState();
        if (lastState != newState) {
            lastState = newState;
            detectAndSendChanges();
        }
    }

    private void detectAndSendChanges() {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack stack = this.slots.get(i).getItem();
            ItemStack itemStack = this.stacks.get(i);

            if (!ItemStack.matches(stack, itemStack)) {
                itemStack = itemStack.isEmpty()? ItemStack.EMPTY: stack.copy();
                this.stacks.set(i, itemStack);

                if (!firstDetection) {
                    for (int j = 0; j < this.listeners.size(); ++i) {
                        ContainerListener listener = this.listeners.get(j);
                        if (listener instanceof ServerPlayer) {
                            sendSlotToPlayer((ServerPlayer) listener, this, i, itemStack);
                        } else {
                            listener.slotChanged(this, i, itemStack);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addSlotListener(ContainerListener listener) {
        if (this.listeners.contains(listener)) {
            throw new IllegalArgumentException("Listener already listening");
        } else {
            this.listeners.add(listener);
            if(listener instanceof ServerPlayer) {
                updateCraftingInventory((ServerPlayer) listener, getItems());
            }
            this.broadcastChanges();
        }
    }


    protected void sendSlotToPlayer(ServerPlayer player, AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
        if (!(containerToSend.getSlot(slotInd) instanceof ResultSlot)) {
            ASMMod._instance.getPacketHandler().sendToPlayer(
                    new ClientboundContainerSetSlotPacketLarge(containerToSend.containerId, getStateId(), slotInd, stack), player);
        }
    }

    @Override
    public int getColumns() {
        return COLUMNS;
    }

    @Override
    public int getPageSize() {
        return ROWS;
    }

    private Slot createSlot(Container inventory, int index, int row, int column) {
        return new SlotExtended(inventory, index, row, column);
    }

    protected int getTagSize(Tag tag) {
        if (tag instanceof NumericTag || tag instanceof EndTag) {
            return 1;
        }
        if (tag instanceof CompoundTag compound) {
            int size = 0;
            for (String key : compound.getAllKeys()) {
                size += getTagSize(compound.get(key));
            }
            return size;
        }
        if (tag instanceof ByteArrayTag) {
            return ((ByteArrayTag) tag).getAsByteArray().length;
        }
        if (tag instanceof IntArrayTag) {
            return ((IntArrayTag) tag).getAsIntArray().length * 32;
        }
        if (tag instanceof ListTag list) {
            int size = 0;
            for (Tag value : list) {
                size += getTagSize(value);
            }
            return size;
        }
        if (tag instanceof StringTag) {
            return ((StringTag) tag).getAsString().getBytes(StandardCharsets.UTF_8).length;
        }
        return tag.toString().length();
    }

    // Modified from ServerPlayer#updateCraftingInventory
    public void updateCraftingInventory(ServerPlayer player, List<ItemStack> allItems) {
        int maxBufferSize = 20000;
        // Custom packet sending to be able to handle large inventories
        ServerGamePacketListenerImpl playerNetServerHandler = player.connection;
        // Modification of logic in EntityPlayerMP#updateCraftingInventory
        CompoundTag sendBuffer = new CompoundTag();
        ListTag sendList = new ListTag();
        sendBuffer.put("stacks", sendList);
        int i = 0;
        int bufferSize = 0;
        int sent = 0;
        for (ItemStack itemStack : allItems) {
            if (itemStack != null) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("slot", i);
                tag.put("stack", itemStack.serializeNBT());
                int tagSize = getTagSize(tag);
                if (bufferSize + tagSize + 100 < maxBufferSize) {
                    sendList.add(tag);
                    bufferSize += tagSize;
                } else {
                    // Flush
                    ASMMod._instance.getPacketHandler().sendToPlayer(new ClientboundContainerSetContentPacketWindow(containerId, getStateId(), sendBuffer), player);
                    sendBuffer = new CompoundTag();
                    sendList = new ListTag();
                    sendList.add(tag);
                    sendBuffer.put("stacks", sendList);
                    bufferSize = tagSize;
                }
            }
            i++;
        }
        if (sendList.size() > 0) {
            // Flush
            ASMMod._instance.getPacketHandler().sendToPlayer(new ClientboundContainerSetContentPacketWindow(containerId, getStateId(), sendBuffer), player);
        }
        playerNetServerHandler.send(new ClientboundContainerSetSlotPacket(-1, getStateId(), -1, getCarried()));
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
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

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
            ASMMod.LOGGER.info("Invalid slotIndex:" + index);
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
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
