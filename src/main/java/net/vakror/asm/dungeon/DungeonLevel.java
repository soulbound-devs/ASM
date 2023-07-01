package net.vakror.asm.dungeon;

import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;
import java.util.Random;

public class DungeonLevel {
    private int rooms;
    private int minMobs;
    private int maxMobs;
    private int[] mobs = new int[0];
    private int level;
    private int size;
    private int currentRoom;

    public DungeonLevel(int rooms, int level, int size, int currentRoom, int minMobs, int maxMobs) {
        this.rooms = rooms;
        this.level = level;
        this.size = size;
        this.currentRoom = currentRoom;
        this.minMobs = minMobs;
        this.maxMobs = maxMobs;
    }

    public DungeonLevel(int rooms, int[] mobs, int level, int size, int currentRoom, int minMobs, int maxMobs) {
        this.rooms = rooms;
        this.mobs = mobs;
        this.level = level;
        this.size = size;
        this.currentRoom = currentRoom;
        this.minMobs = minMobs;
        this.maxMobs = maxMobs;
    }

    private int[] generateRandomMobCount(int minMobs, int maxMobs, int count) {
        int[] mobs = new int[count - 1];
        for (int i = 0; i < count; i++) {
            mobs[i] = new Random().nextInt(minMobs, maxMobs + 1);
        }
        System.out.println(Arrays.toString(mobs));
        return mobs;
    }

    public CompoundTag serializeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("rooms", this.rooms);
        tag.putIntArray("mobs", this.mobs());
        tag.putInt("level", this.level);
        tag.putInt("size", this.size);
        tag.putInt("currentRoom", this.currentRoom);
        tag.putInt("minMobs", this.minMobs);
        tag.putInt("maxMobs", this.maxMobs);
        return tag;
    }

    public static DungeonLevel deserializeNbt(CompoundTag tag) {
        return new DungeonLevel(tag.getInt("rooms"), tag.getIntArray("mobs"), tag.getInt("level"), tag.getInt("size"), tag.getInt("currentRoom"), tag.getInt("minMobs"), tag.getInt("maxMobs"));
    }

    public int rooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int[] mobs() {
        if (mobs.length == 0) {
            mobs = generateRandomMobCount(this.minMobs, this.maxMobs, this.rooms);
        }
        return mobs;
    }

    public int mobs(int index) {
        if (mobs.length == 0) {
            mobs = generateRandomMobCount(this.minMobs, this.maxMobs, this.rooms);
        }
        return mobs[index - 1];
    }

    @Override
    public String toString() {
        return "DungeonLevel{" +
                "rooms=" + rooms +
                ", mobs=" + Arrays.toString(mobs) +
                ", level=" + level +
                ", size=" + size +
                ", currentRoom=" + currentRoom +
                '}';
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
