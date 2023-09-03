package net.vakror.soulbound.mod.compat.hammerspace.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.mod.compat.hammerspace.blocks.ModDungeonBlocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ReturnToOverWorldBlockEntity extends BlockEntity{
    private boolean hasGeneratedDungeon = false;
    private boolean needsToModifySpawnPos = true;
    private int unstableDungeonReturnMessageDelay = 0;
    private BlockPos spawnPos = new BlockPos(0, 64, 0);

    public ReturnToOverWorldBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModDungeonBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public ReturnToOverWorldBlockEntity(BlockPos pPos, BlockState pBlockState, BlockPos spawnPos) {
        super(ModDungeonBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.spawnPos = spawnPos;
    }

    public int unstableDungeonReturnMessageDelay() {
        return unstableDungeonReturnMessageDelay;
    }

    public void setUnstableDungeonReturnMessageDelay(int unstableDungeonReturnMessageDelay) {
        this.unstableDungeonReturnMessageDelay = unstableDungeonReturnMessageDelay;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putBoolean("hasGeneratedDungeon", hasGeneratedDungeon);
        nbt.putBoolean("needsToModifySpawnPos", needsToModifySpawnPos);
        nbt.putIntArray("spawnPos", new int[]{spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()});

        super.saveAdditional(nbt);
    }

    @Override
    @SuppressWarnings("all")
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        hasGeneratedDungeon = pTag.getBoolean("hasGeneratedDungeon");
        needsToModifySpawnPos = pTag.getBoolean("needsToModifySpawnPos");
        if (pTag.getIntArray("spawnPos") != null && pTag.getIntArray("spawnPos").length == 3) {
            spawnPos = new BlockPos(pTag.getIntArray("spawnPos")[0], pTag.getIntArray("spawnPos")[1], pTag.getIntArray("spawnPos")[2]);
        } else {
            spawnPos = new BlockPos(0, 64, 0);
        }
    }

    public ItemStack drops() {
        CompoundTag droppedBlockNbt;
        droppedBlockNbt = this.saveWithoutMetadata();
        return new ItemStack(ModDungeonBlocks.DUNGEON_KEY_BLOCK.get(), 1, droppedBlockNbt);
    }

    public boolean hasGeneratedDungeon() {
        return hasGeneratedDungeon;
    }

    public void hasGeneratedDungeon(boolean value) {
        hasGeneratedDungeon = value;
    }

    public int getDungeonSize() {
        return 50;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ReturnToOverWorldBlockEntity entity) {
        if (entity.unstableDungeonReturnMessageDelay > 0) {
            entity.setUnstableDungeonReturnMessageDelay(entity.unstableDungeonReturnMessageDelay - 1);
        }
    }

    @Nullable
    public BlockPos getSpawnPos() {
        return spawnPos;
    }

    public void setSpawnPos(@Nullable BlockPos spawnPos) {
        this.spawnPos = spawnPos;
    }

    public boolean needsToModifySpawnPos() {
        return needsToModifySpawnPos;
    }

    public void setNeedsToModifySpawnPos(boolean needsToModifySpawnPos) {
        this.needsToModifySpawnPos = needsToModifySpawnPos;
    }
}
