package net.vakror.soulbound.seal.seals.activatable.tool;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.SoulboundMod;

public class HoeingSeal extends ToolSeal {
    public HoeingSeal() {
        super(new ResourceLocation(SoulboundMod.MOD_ID, "hoeing"), BlockTags.MINEABLE_WITH_HOE);
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public float getDamage() {
        return 1f;
    }
}
