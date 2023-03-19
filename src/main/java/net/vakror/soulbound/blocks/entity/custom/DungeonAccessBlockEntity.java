package net.vakror.soulbound.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.blocks.ModBlocks;
import net.vakror.soulbound.blocks.entity.ModBlockEntities;

public class DungeonAccessBlockEntity extends BlockEntity{
    private long dimensionUUID = 0;

    public DungeonAccessBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DUNGEON_ACCESS_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putLong("uuid", dimensionUUID);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        dimensionUUID = pTag.getLong("uuid");
    }

    public long getDimensionUUID() {
        return dimensionUUID;
    }

    public void setDimensionUUID(long dimensionUUID) {
        this.dimensionUUID = dimensionUUID;
    }

    public ItemStack drops() {
        CompoundTag droppedBlockNbt;
        droppedBlockNbt = this.saveWithoutMetadata();
        return new ItemStack(ModBlocks.DUNGEON_KEY_BLOCK.get(), 1, droppedBlockNbt);
    }
}
