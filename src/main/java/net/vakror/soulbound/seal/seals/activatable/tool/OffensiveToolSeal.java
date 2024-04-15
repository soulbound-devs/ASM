package net.vakror.soulbound.seal.seals.activatable.tool;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vakror.soulbound.seal.SealType;

public abstract class OffensiveToolSeal extends ToolSeal {

    public OffensiveToolSeal(ResourceLocation id, TagKey<Block> mineableBlocks, float swingSpeed) {
        super(id, mineableBlocks, swingSpeed);
    }

    @Override
    public boolean isAttack() {
        return true;
    }

    @Override
    public SealType getType() {
        return SealType.OFFENSIVE;
    }
}
