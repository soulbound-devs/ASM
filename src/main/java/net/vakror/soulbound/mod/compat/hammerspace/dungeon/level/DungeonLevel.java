package net.vakror.soulbound.mod.compat.hammerspace.dungeon.level;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;

public class DungeonLevel {
    public static Multimap<Pair<Integer, Integer>, DungeonLevel> ALL_LEVELS = LinkedListMultimap.create();

    protected int size;
    protected int level;
    protected String name;

    public DungeonLevel(int size, int level, String name) {
        this.size = size;
        this.level = level;
        this.name = name;
    }

    public CompoundTag serializeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", size);
        tag.putInt("level", level);
        tag.putString("name", name);
        return tag;
    }
    public DungeonLevel deserializeNbt(CompoundTag tag) {
        size = tag.getInt("size");
        level = tag.getInt("level");
        name = tag.getString("name");
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
