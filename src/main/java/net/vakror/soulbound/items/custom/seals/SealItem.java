package net.vakror.soulbound.items.custom.seals;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.vakror.soulbound.seal.SealType;
import net.vakror.soulbound.seal.Tooltip;
import net.vakror.soulbound.seal.tier.sealable.ISealableTier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class SealItem extends Item {
    private final ResourceLocation id;
    private final SealType type;
    private final ToIntFunction<ISealableTier> maxSealStack;
    private final Tooltip tooltip;

    public SealType getType() {
        return type;
    }

    public ResourceLocation getId() {
        return id;
    }

    public SealItem(Properties pProperties, ResourceLocation id, SealType type) {
        this(pProperties, id, type, Tooltip.empty());
    }

    public SealItem(Properties pProperties, ResourceLocation id, SealType type, Tooltip tooltip) {
        this(pProperties, id, type, false, 1, tooltip);
    }

    public SealItem(Properties pProperties, ResourceLocation id, SealType type, boolean canAddMultiple, int maxSealStack, Tooltip tooltip) {
        this(pProperties, id, type, (tier -> canAddMultiple ? maxSealStack: 1),tooltip);
    }

    public SealItem(Properties pProperties, ResourceLocation id, SealType type, ToIntFunction<ISealableTier> sealableFunction, Tooltip tooltip) {
        super(pProperties);
        this.id = id;
        this.type = type;
        this.maxSealStack = sealableFunction;
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

    public boolean canAddMultiple(ISealableTier tier) {
        return maxSealStack.applyAsInt(tier) > 1;
    }

    public ToIntFunction<ISealableTier> getMaxSealStack() {
        return maxSealStack;
    }
}
