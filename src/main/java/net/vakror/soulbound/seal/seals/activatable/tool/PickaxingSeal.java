package net.vakror.soulbound.seal.seals.activatable.tool;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.SoulboundMod;

public class PickaxingSeal extends ToolSeal {
    public PickaxingSeal() {
        super(new ResourceLocation(SoulboundMod.MOD_ID, "pickaxing"), BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public float getDamage() {
        return 4.5f;
    }
}
