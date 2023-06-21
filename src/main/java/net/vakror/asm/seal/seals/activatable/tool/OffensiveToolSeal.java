package net.vakror.asm.seal.seals.activatable.tool;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.AttackSeal;

import java.util.List;

public abstract class OffensiveToolSeal extends AttackSeal {
    public final TagKey<Block> mineableBlocks;



    public OffensiveToolSeal(String id, TagKey<Block> mineableBlocks, float swingSpeed) {
        super(id, swingSpeed);
        this.mineableBlocks = mineableBlocks;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("tool"));
        return super.properties();
    }
}
