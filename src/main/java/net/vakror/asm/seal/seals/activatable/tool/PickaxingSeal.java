package net.vakror.asm.seal.seals.activatable.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.SealProperty;

import java.util.List;

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

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("passive"));
        return super.properties();
    }
}
