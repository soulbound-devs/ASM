package net.vakror.soulbound.compat.hammerspace.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.compat.hammerspace.blocks.ModDungeonBlocks;

public class ReturnToOverWorldBlockEntity extends BlockEntity{
    private boolean hasGeneratedDungeon = false;
    private int unstableDungeonReturnMessageDelay = 0;

    public ReturnToOverWorldBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModDungeonBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), pPos, pBlockState);
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

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        hasGeneratedDungeon = pTag.getBoolean("hasGeneratedDungeon");
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
}
