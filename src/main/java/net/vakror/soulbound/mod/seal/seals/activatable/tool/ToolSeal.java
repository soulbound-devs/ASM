package net.vakror.soulbound.mod.seal.seals.activatable.tool;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vakror.soulbound.mod.seal.SealProperty;
import net.vakror.soulbound.mod.seal.SealType;
import net.vakror.soulbound.mod.seal.type.ActivatableSeal;

import java.util.List;

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
    public List<SealProperty> properties() {
        properties.add(new SealProperty("tool"));
        return super.properties();
    }

    @Override
    public SealType getType() {
        return SealType.PASSIVE;
    }
}
