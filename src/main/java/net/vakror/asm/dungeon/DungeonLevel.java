package net.vakror.asm.dungeon;

import net.minecraft.nbt.CompoundTag;

public class DungeonLevel {
    private int rooms;
    private int[] mobs;
    private int level;
    private int size;
    private int currentRoom;

    public DungeonLevel(int rooms, int[] mobs, int level, int size, int currentRoom) {
        this.rooms = rooms;
        this.mobs = mobs;
        this.level = level;
        this.size = size;
        this.currentRoom = currentRoom;
    }

    public CompoundTag serializeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("rooms", this.rooms);
        tag.putIntArray("mobs", this.mobs);
        tag.putInt("level", this.level);
        tag.putInt("size", this.size);
        return tag;
    }

    public static DungeonLevel deserializeNbt(CompoundTag tag) {
        return new DungeonLevel(tag.getInt("rooms"), tag.getIntArray("mobs"), tag.getInt("level"), tag.getInt("size"), tag.getInt("currentRoom"));
    }

    public int rooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int[] mobs() {
        return mobs;
    }

    public int mobs(int index) {
        return mobs[index - 1];
    }

    public void setMobs(int[] mobs) {
        this.mobs = mobs;
    }

    public void setMobAmount(int index, int amount) {
        this.mobs[index] = amount;
    }

    public void removeOneMobFromRoom(int roomIndex) {
        this.mobs[roomIndex - 1] = this.mobs[roomIndex - 1] - 1;
    }

    public int level() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int size() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int currentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }
}
