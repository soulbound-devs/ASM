package net.vakror.asm.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.asm.blocks.ModBlocks;
import net.vakror.asm.blocks.entity.ModBlockEntities;

import java.util.UUID;

public class DungeonAccessBlockEntity extends BlockEntity{
    private UUID dimensionUUID;

    public DungeonAccessBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DUNGEON_ACCESS_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, DungeonAccessBlockEntity blockEntity) {
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
        CompoundTag droppedBlockNbt;
        droppedBlockNbt = this.saveWithoutMetadata();
        return new ItemStack(ModBlocks.DUNGEON_KEY_BLOCK.get(), 1, droppedBlockNbt);
    }
}
