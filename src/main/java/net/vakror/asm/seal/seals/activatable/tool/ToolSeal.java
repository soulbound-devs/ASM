package net.vakror.asm.seal.seals.activatable.tool;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.ActivatableSeal;

import java.util.List;

public abstract class ToolSeal extends ActivatableSeal {
    public final TagKey<Block> mineableBlocks;

    public ToolSeal(String id, TagKey<Block> mineableBlocks) {
        super(id, 2);
        this.mineableBlocks = mineableBlocks;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("tool"));
        return super.properties();
    }
}
