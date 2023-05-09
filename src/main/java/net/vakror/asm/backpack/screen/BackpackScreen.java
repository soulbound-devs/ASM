package net.vakror.asm.backpack.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vakror.asm.ASMMod;
import org.cyclops.cyclopscore.client.gui.component.button.ButtonArrow;
import org.cyclops.cyclopscore.client.gui.container.ContainerScreenScrolling;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BackpackScreen extends ContainerScreenScrolling<BackpackMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ASMMod.MOD_ID, "textures/gui/backpack_gui.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 223;

    public BackpackScreen(BackpackMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(new ButtonArrow(this.leftPos + 173, this.topPos + 7, Component.translatable("gui.cyclopscore.up"), (button) -> scrollRelative(1), ButtonArrow.Direction.NORTH));
        addRenderableWidget(new ButtonArrow(this.leftPos + 173, this.topPos + 129, Component.translatable("gui.cyclopscore.down"), (button) -> scrollRelative(-1), ButtonArrow.Direction.SOUTH));
    }

    protected void scrollRelative(int direction) {
        assert Minecraft.getInstance().player != null;
        int multiplier = Minecraft.getInstance().player.isCrouching() ? 9: 1;
        this.getScrollbar().scrollRelative(direction * multiplier);
    }

    @Override
    protected boolean isSearchEnabled() {
        return false;
    }

    @Override
    protected boolean isSubsetRenderSlots() {
        return true;
    }

    @Override
    protected ResourceLocation constructGuiTexture() {
        return BACKGROUND_TEXTURE;
    }

    @Override
    protected int getBaseXSize() {
        return WIDTH;
    }

    @Override
    protected int getBaseYSize() {
        return HEIGHT;
    }

    public void drawString(PoseStack matrices) {
        font.draw(matrices, getTitle().getString(), 8 + offsetX, 6 + offsetY, 4210752);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack matrices, int mouseX, int mouseY) {
        drawString(matrices);
    }

    @Override
    protected void slotClicked(Slot slotIn, int slotId, int clickedButton, @NotNull ClickType clickType) {
        slotId = slotIn.index;
        // Send our own packet, to avoid C0EPacketClickWindow to be sent to the server what would trigger an overflowable S30PacketWindowItems
        handleInventoryMouseClick(this.container.containerId, slotId, clickedButton, clickType, Objects.requireNonNull(this.getMinecraft().player));
    }

    // Adapted from MultiPlayerGameMode#handleInventoryMouseClick
    protected void handleInventoryMouseClick(int windowId, int slotId, int mouseButtonClicked, ClickType clickType, Player playerIn) {
        AbstractContainerMenu abstractcontainermenu = playerIn.containerMenu;
        NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
        int i = nonnulllist.size();
        List<ItemStack> list = Lists.newArrayListWithCapacity(i);

        for(Slot slot : nonnulllist) {
            list.add(slot.getItem().copy());
        }

        abstractcontainermenu.clicked(slotId, mouseButtonClicked, clickType, playerIn);
        Int2ObjectMap<ItemStack> changedSlots = new Int2ObjectOpenHashMap<>();

        for(int j = 0; j < i; ++j) {
            ItemStack itemstack = list.get(j);
            ItemStack itemstack1 = nonnulllist.get(j).getItem();
            if (!ItemStack.matches(itemstack, itemstack1)) {
                changedSlots.put(j, itemstack1.copy());
            }
        }

        // Original: this.connection.send(new ServerboundContainerClickPacket(p_171800_, abstractcontainermenu.getStateId(), p_171801_, p_171802_, p_171803_, abstractcontainermenu.getCarried().copy(), changedSloits));
        ASMMod._instance.getPacketHandler().sendToServer(new ServerboundContainerClickPacketOverride(windowId, abstractcontainermenu.getStateId(), slotId, mouseButtonClicked, clickType, abstractcontainermenu.getCarried().copy(), changedSlots));
    }
}
