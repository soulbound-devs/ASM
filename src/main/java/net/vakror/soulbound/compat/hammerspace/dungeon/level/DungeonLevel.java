package net.vakror.soulbound.compat.hammerspace.dungeon.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

public class DungeonLevel {

    public static final Codec<DungeonLevel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("size").forGetter(DungeonLevel::size),
            Codec.INT.fieldOf("level").forGetter(DungeonLevel::level)
    ).apply(instance, DungeonLevel::new));
    protected int size;
    protected int level;

    public DungeonLevel(int size, int level) {
        this.size = size;
        this.level = level;
    }

    public CompoundTag serializeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", size);
        tag.putInt("level", level);
        return tag;
    }
    public DungeonLevel deserializeNbt(CompoundTag tag) {
        size = tag.getInt("size");
        level = tag.getInt("level");
        return this;
    }

    public int size() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int level() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
