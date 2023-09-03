package net.vakror.soulbound.mod.seal.seals.activatable.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

public class AxingSeal extends OffensiveToolSeal {
    public AxingSeal() {
        super("axing", BlockTags.MINEABLE_WITH_AXE, 3.5f);
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
