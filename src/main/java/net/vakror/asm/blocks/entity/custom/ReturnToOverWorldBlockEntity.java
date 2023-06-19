package net.vakror.asm.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.blocks.entity.ModBlockEntities;

public class ReturnToOverWorldBlockEntity extends BlockEntity{
    private boolean hasGeneratedDungeon = false;

    public ReturnToOverWorldBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), pPos, pBlockState);
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
        return new ItemStack(ModBlocks.DUNGEON_KEY_BLOCK.get(), 1, droppedBlockNbt);
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
}
