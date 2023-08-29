package net.vakror.soulbound.mod.compat.hammerspace.dungeon.level.room.multi;

import net.minecraft.nbt.CompoundTag;
import net.vakror.soulbound.mod.compat.hammerspace.dungeon.level.DungeonLevel;

import java.util.Arrays;

public class MultiRoomDungeonLevel extends DungeonLevel {
    private int rooms;
    private int[] mobs = new int[0];
    /**
     * Value from 1 - {@link #rooms}
     */
    private int currentRoom;

    public MultiRoomDungeonLevel(int rooms, int level, int size, int currentRoom) {
        super(size, level);
        this.rooms = rooms;
        this.mobs = new int[rooms];
        this.currentRoom = currentRoom;
    }

    public MultiRoomDungeonLevel(int rooms, int[] mobs, int level, int size, int currentRoom) {
        super(size, level);
        this.rooms = rooms;
        this.mobs = mobs;
        this.currentRoom = currentRoom;
    }

    @Override
    public CompoundTag serializeNbt() {
        CompoundTag tag = super.serializeNbt();
        tag.putInt("rooms", this.rooms);
        tag.putIntArray("mobs", this.mobs());
        tag.putInt("currentRoom", this.currentRoom);
        return tag;
    }

    @Override
    public DungeonLevel deserializeNbt(CompoundTag tag) {
        this.rooms = tag.getInt("rooms");
        this.mobs = tag.getIntArray("mobs");
        this.currentRoom = tag.getInt("currentRoom");
        return this;
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
