package net.vakror.soulbound.compat.dungeon.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import org.jetbrains.annotations.NotNull;

public class ReturnToOverWorldBlockEntity extends BlockEntity{
    private int cannotExitMessageDelay = 0;

    public ReturnToOverWorldBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModDungeonBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public ReturnToOverWorldBlockEntity(BlockPos pPos, BlockState pBlockState, BlockPos spawnPos) {
        super(ModDungeonBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public int getCannotExitMessageDelay() {
        return cannotExitMessageDelay;
    }

    public void setCannotExitMessageDelay(int cannotExitMessageDelay) {
        this.cannotExitMessageDelay = cannotExitMessageDelay;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }

    @Override
    @SuppressWarnings("all")
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
    }

    public ItemStack drops() {
        CompoundTag droppedBlockNbt;
        droppedBlockNbt = this.saveWithoutMetadata();
        return new ItemStack(ModDungeonBlocks.DUNGEON_KEY_BLOCK.get(), 1, droppedBlockNbt);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ReturnToOverWorldBlockEntity entity) {
        if (entity.cannotExitMessageDelay > 0) {
            entity.setCannotExitMessageDelay(entity.cannotExitMessageDelay - 1);
        }
    }
}
