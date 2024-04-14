package net.vakror.soulbound.compat.dungeon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Dungeon implements INBTSerializable<CompoundTag> {
    boolean enterable = true;
    boolean hasFirstTickElapsed = false;
    boolean hasGenerated = false;
    boolean deleteAfterExiting = true;

    @Nullable String cannotExitMessage = "Cannot Exit Dungeon!";

    ResourceLocation type;

    public Dungeon(boolean enterable, boolean hasFirstTickElapsed, boolean hasGenerated, boolean deleteAfterExiting, @Nullable String cannotExitMessage, ResourceLocation type) {
        this.enterable = enterable;
        this.hasFirstTickElapsed = hasFirstTickElapsed;
        this.hasGenerated = hasGenerated;
        this.deleteAfterExiting = deleteAfterExiting;
        this.cannotExitMessage = cannotExitMessage;
        this.type = type;
    }

    public Dungeon() {
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("enterable", enterable);
        tag.putString("type", type.toString());
        tag.putBoolean("firstTick", hasFirstTickElapsed);
        tag.putBoolean("deleteAfterExiting", deleteAfterExiting);
        tag.putBoolean("hasGenerated", hasGenerated);
        if (cannotExitMessage != null) {
            tag.putString("cannotExitMessage", cannotExitMessage);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(@NotNull CompoundTag tag) {
        Dungeon.deserialize(tag);
    }

    public static Dungeon deserialize(CompoundTag tag) {
        Dungeon dungeon = new Dungeon();
        dungeon.enterable = tag.getBoolean("enterable");
        dungeon.type = new ResourceLocation(tag.getString("type"));
        dungeon.hasFirstTickElapsed = tag.getBoolean("firstTick");
        dungeon.hasGenerated = tag.getBoolean("hasGenerated");
        dungeon.deleteAfterExiting = tag.getBoolean("deleteAfterExiting");
        if (tag.get("cannotExitMessage") != null) {
            dungeon.cannotExitMessage = tag.getString("cannotExitMessage");
        }
        return dungeon;
    }

    public boolean isEnterable() {
        return enterable;
    }

    public void setEnterable(boolean canEnter) {
        this.enterable = canEnter;
    }

    public String getJoinMessage(ServerPlayer player, ServerLevel level) {
        return "§8" + player.getDisplayName() + "§F Has Joined a Dungeon";
    }

    public ResourceLocation getType() {
        return type;
    }

    public void setType(ResourceLocation type) {
        this.type = type;
    }

    public boolean hasFirstTickElapsed() {
        return hasFirstTickElapsed;
    }

    public void setHasFirstTickElapsed(boolean hasFirstTickElapsed) {
        this.hasFirstTickElapsed = hasFirstTickElapsed;
    }

    public boolean shouldDeleteAfterExiting() {
        return deleteAfterExiting;
    }

    public void setDeleteAfterExiting(boolean deleteAfterExiting) {
        this.deleteAfterExiting = deleteAfterExiting;
    }

    public @Nullable String getCannotExitMessage() {
        return cannotExitMessage;
    }

    public void setCannotExitMessage(@Nullable String cannotExitMessage) {
        this.cannotExitMessage = cannotExitMessage;
    }

    public boolean hasGenerated() {
        return hasGenerated;
    }

    public void setHasGenerated(boolean hasGenerated) {
        this.hasGenerated = hasGenerated;
    }

    /**
     * Override if you are making an implementation
     * @return a copy of this dungeon
     */
    public Dungeon copy() {
        return new Dungeon(enterable, hasFirstTickElapsed, hasGenerated, deleteAfterExiting, cannotExitMessage, type);
    }
}
