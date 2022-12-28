package net.vakror.soulbound.items.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.SoulBoundMod;

public class WandItem extends DiggerItem {
    public WandItem(Properties properties) {
        super(3, -3, Tiers.DIAMOND, BlockTags.create(new ResourceLocation(SoulBoundMod.MOD_ID, "none")), properties);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        boolean hasAxing = hasSeal("axing");
        boolean hasPickaxing = hasSeal("pickaxing");
        boolean hasHoeing = hasSeal("hoeing");

        if (hasAxing && state.is(BlockTags.MINEABLE_WITH_AXE)) {
            return true;
        }
        if (hasPickaxing && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return true;
        }
        return hasHoeing && state.is(BlockTags.MINEABLE_WITH_HOE);
    }

    public boolean hasSeal(String sealID) {
        return false;
    }
}
