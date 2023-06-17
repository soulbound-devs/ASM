package net.vakror.asm.items.custom.seals;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.SealTooltip;
import net.vakror.asm.seal.SealType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SealItem extends Item {
    private final String id;
    private final SealType type;
    private final boolean canAddMultiple;
    private final int maxSealStack;
    private final SealTooltip tooltip;

    public SealType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public SealItem(Properties pProperties, String id, SealType type) {
        this(pProperties, id, type, false, 1, SealTooltip.empty());
    }

    public SealItem(Properties pProperties, String id, SealType type, SealTooltip tooltip) {
        this(pProperties, id, type, false, 1, tooltip);
    }

    public SealItem(Properties pProperties, String id, SealType type, boolean canAddMultiple, int maxSealStack, SealTooltip tooltip) {
        super(pProperties);
        this.id = id;
        this.type = type;
        this.canAddMultiple = canAddMultiple;
        this.maxSealStack = maxSealStack;
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.literal("Press §eSHIFT§r for more Information"));
        } else {
            tooltip.addAll(this.tooltip.tooltip());
        }
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
    }

    public boolean canAddMultiple() {
        return canAddMultiple;
    }

    public int getMaxSealStack() {
        return maxSealStack;
    }
}
