package net.vakror.asm.seal.seals.activatable.tool;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vakror.asm.seal.SealProperty;

import java.util.List;

public abstract class OffensiveToolSeal extends ToolSeal {

    public OffensiveToolSeal(String id, TagKey<Block> mineableBlocks, float swingSpeed) {
        super(id, mineableBlocks, swingSpeed);
    }

    @Override
    public boolean isAttack() {
        return true;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("tool"));
        return super.properties();
    }
}
