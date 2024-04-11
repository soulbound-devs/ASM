package net.vakror.soulbound.seal.seals.activatable.tool;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vakror.soulbound.seal.SealType;
import net.vakror.soulbound.seal.type.ActivatableSeal;

public abstract class ToolSeal extends ActivatableSeal {
    public final TagKey<Block> mineableBlocks;

    public ToolSeal(String id, TagKey<Block> mineableBlocks) {
        super(id, 2f);
        this.mineableBlocks = mineableBlocks;
    }

    public ToolSeal(String id, TagKey<Block> mineableBlocks, float swingSpeed) {
        super(id, swingSpeed);
        this.mineableBlocks = mineableBlocks;
    }

    @Override
    public SealType getType() {
        return SealType.PASSIVE;
    }
}
