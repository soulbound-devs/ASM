package net.vakror.soulbound.mod.seal.seals.activatable.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class PickaxingSeal extends ToolSeal {
    public PickaxingSeal() {
        super("pickaxing", BlockTags.MINEABLE_WITH_PICKAXE);
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
