package net.vakror.soulbound.compat.hammerspace.dungeon.level.room.multi;

import net.minecraft.nbt.CompoundTag;
import net.vakror.soulbound.compat.hammerspace.dungeon.level.DungeonLevel;

import java.util.Arrays;
import java.util.Random;

public class MultiRoomDungeonLevel extends DungeonLevel {
    private int rooms;
    private int minMobs;
    private int maxMobs;
    private int[] mobs = new int[0];
    private int currentRoom;

    public MultiRoomDungeonLevel(int rooms, int level, int size, int currentRoom, int minMobs, int maxMobs) {
        super(size, level);
        this.rooms = rooms;
        this.currentRoom = currentRoom;
        this.minMobs = minMobs;
        this.maxMobs = maxMobs;
    }

    public MultiRoomDungeonLevel(int rooms, int[] mobs, int level, int size, int currentRoom, int minMobs, int maxMobs) {
        super(size, level);
        this.rooms = rooms;
        this.mobs = mobs;
        this.currentRoom = currentRoom;
        this.minMobs = minMobs;
        this.maxMobs = maxMobs;
    }

    private int[] generateRandomMobCount(int minMobs, int maxMobs, int count) {
        int[] mobs = new int[count];
        for (int i = 0; i < count; i++) {
            mobs[i] = new Random().nextInt(minMobs, maxMobs + 1);
        }
        System.out.println("HAD TO GENERATE MOB COUNT");
        return mobs;
    }

    @Override
    public CompoundTag serializeNbt() {
        CompoundTag tag = super.serializeNbt();
        tag.putInt("rooms", this.rooms);
        tag.putIntArray("mobs", this.mobs());
        tag.putInt("currentRoom", this.currentRoom);
        tag.putInt("minMobs", this.minMobs);
        tag.putInt("maxMobs", this.maxMobs);
        return tag;
    }

    @Override
    public DungeonLevel deserializeNbt(CompoundTag tag) {
        this.rooms = tag.getInt("rooms");
        this.mobs = tag.getIntArray("mobs");
        this.currentRoom = tag.getInt("currentRoom");
        this.minMobs = tag.getInt("minMobs");
        this.maxMobs = tag.getInt("maxMobs");
        return this;
    }

    public int rooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int[] mobs() {
        if (mobs.length == 0 || mobs.length != rooms) {
            mobs = generateRandomMobCount(this.minMobs, this.maxMobs, this.rooms);
        } else {
            System.out.println("DID NOT HAVE TO GENERATE MOB COUNT");
        }
        return mobs;
    }

    public int mobs(int index) {
        if (mobs.length == 0 || mobs.length != rooms) {
            mobs = generateRandomMobCount(this.minMobs, this.maxMobs, this.rooms);
        } else {
            System.out.println("DID NOT HAVE TO GENERATE MOB COUNT");
        }
        return mobs[index - 1];
    }

    @Override
    public String toString() {
        return "DungeonLevel{" +
                "rooms=" + rooms +
                ", mobs=" + Arrays.toString(mobs()) +
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

    public int currentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(int currentRoom) {
        this.currentRoom = currentRoom;
    }
}
