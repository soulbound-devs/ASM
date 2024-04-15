package net.vakror.soulbound.seal.seals.activatable.tool;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.SoulboundMod;

public class AxingSeal extends OffensiveToolSeal {
    public AxingSeal() {
        super(new ResourceLocation(SoulboundMod.MOD_ID,"axing"), BlockTags.MINEABLE_WITH_AXE, 3.5f);
    }

    @Override
    public float getDamage() {
        return 9f;
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return Items.DIAMOND_AXE.useOn(context);
    }
}
