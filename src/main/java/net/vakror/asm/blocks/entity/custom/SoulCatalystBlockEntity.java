package net.vakror.asm.blocks.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vakror.asm.blocks.entity.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

public class SoulCatalystBlockEntity extends BlockEntity {
    protected final ContainerData data;
    private int delay = 0;

    private int maxDelay = secondsToTicks(2);

    public SoulCatalystBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SOUL_CATALYST_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SoulCatalystBlockEntity.this.delay;
                    case 1 -> SoulCatalystBlockEntity.this.maxDelay;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SoulCatalystBlockEntity.this.delay = pValue;
                    case 1 -> SoulCatalystBlockEntity.this.maxDelay = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("delay", delay);
        pTag.putInt("maxDelay", maxDelay);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        delay = pTag.getInt("delay");
        maxDelay = pTag.getInt("maxDelay");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SoulCatalystBlockEntity entity) {
        if (entity.delay > 0) {
            entity.delay--;
        }
    }

    private int secondsToTicks(int seconds) {
        return seconds * 20;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
    }
}
