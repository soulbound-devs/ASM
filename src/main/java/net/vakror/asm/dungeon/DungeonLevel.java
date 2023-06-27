package net.vakror.asm.dungeon;

import net.minecraft.nbt.CompoundTag;

public record DungeonLevel(int rooms, int[] mobs, int level, int size) {

    public CompoundTag serializeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("rooms", this.rooms);
        tag.putIntArray("mobs", this.mobs);
        tag.putInt("level", this.level);
        tag.putInt("size", this.size);
        return tag;
    }
    
    public static DungeonLevel deserializeNbt(CompoundTag tag) {
        return new DungeonLevel(tag.getInt("rooms"), tag.getIntArray("mobs"), tag.getInt("level"), tag.getInt("size"));
    }
}
