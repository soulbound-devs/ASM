package net.vakror.asm.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.entity.custom.DungeonAccessBlockEntity;
import net.vakror.asm.dungeon.capability.DungeonProvider;
import net.vakror.asm.items.ModItems;
import net.vakror.asm.world.dimension.DungeonTeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.vakror.asm.world.dimension.DimensionUtils.createWorld;
import static net.vakror.asm.world.dimension.DimensionUtils.doesLevelExist;

public class DungeonAccessBlock extends BaseEntityBlock {
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DungeonAccessBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean shouldTeleport = true;
        if (!level.isClientSide && player.getItemInHand(hand).getItem().equals(ModItems.KEY.get()) && state.getValue(LOCKED)) {
            level.setBlock(pos, state.setValue(LOCKED, false), 35);
            player.getItemInHand(hand).shrink(1);
            shouldTeleport = !shouldTeleport;
        }
        if (!level.isClientSide && !state.getValue(LOCKED) && shouldTeleport) {
            /*
            player teleportation
            */
            if (level.getBlockEntity(pos) instanceof DungeonAccessBlockEntity blockEntity) {
                if (blockEntity.getDimensionUUID() == null) {
                    blockEntity.setDimensionUUID(UUID.randomUUID());
                    blockEntity.setChanged();
                }
                boolean levelExists = doesLevelExist(ASMMod.instance.server, blockEntity.getDimensionUUID());
                final boolean[] canEnter = new boolean[]{true};
                ServerLevel dimension = createWorld(level, blockEntity);
                    dimension.getCapability(DungeonProvider.DUNGEON).ifPresent((dungeonLevel -> {
                        if (!levelExists) {
                            dungeonLevel.setStable(new Random().nextBoolean());
                        }
                        canEnter[0] = dungeonLevel.canEnter();
                    }));
                if (canTeleport(level, player, dimension, blockEntity.getDimensionUUID()) && canEnter[0]) {
                    player.setPortalCooldown();
                    player.changeDimension(dimension, new DungeonTeleporter(pos, this));
                }
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    private boolean canTeleport(Level level, Player player, ServerLevel dimension, UUID dimensionUUID) {
        return dimension.players().isEmpty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LOCKED);
        builder.add(FACING);
    }
    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pBuilder) {
        if (pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof DungeonAccessBlockEntity blockEntity) {
            return new ArrayList<>(Collections.singleton(blockEntity.drops()));
        }
        return super.getDrops(pState, pBuilder);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pLevel.getBlockEntity(pPos) instanceof DungeonAccessBlockEntity blockEntity) {
            if (pStack.hasTag() && pStack.getTag().hasUUID("uuid")) {
                blockEntity.setDimensionUUID(pStack.getTag().getUUID("uuid"));
                blockEntity.setChanged();
            }
        }
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DungeonAccessBlockEntity(pPos, pState);
    }
}
