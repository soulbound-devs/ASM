package net.vakror.soulbound.compat.dungeon.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;

import java.util.UUID;

public class DungeonAccessBlockEntity extends BlockEntity{
    private UUID dimensionUUID;
    public DungeonAccessBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModDungeonBlockEntities.DUNGEON_ACCESS_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        if (dimensionUUID == null) {
            dimensionUUID = UUID.randomUUID();
        }
        nbt.putUUID("uuid", dimensionUUID);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        dimensionUUID = pTag.getUUID("uuid");
    }

    public UUID getDimensionUUID() {
        return dimensionUUID;
    }

    public void setDimensionUUID(UUID dimensionUUID) {
        this.dimensionUUID = dimensionUUID;
    }

    public ItemStack drops() {
        ItemStack stack = new ItemStack(ModDungeonBlocks.DUNGEON_KEY_BLOCK.get(), 1);
        BlockItem.setBlockEntityData(stack, ModDungeonBlockEntities.DUNGEON_ACCESS_BLOCK_ENTITY.get(), this.saveWithoutMetadata());
        return stack;
    }
}
