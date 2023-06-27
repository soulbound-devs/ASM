package net.vakror.asm.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.entity.custom.ReturnToOverWorldBlockEntity;
import net.vakror.asm.world.dimension.DungeonTeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReturnToOverworldBlock extends BaseEntityBlock {
    public ReturnToOverworldBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            /*
            player teleportation
            */
            ServerLevel dimension = ASMMod.instance.server.getLevel(ServerLevel.OVERWORLD);
            player.setPortalCooldown();
            if (dimension == null) {
                throw new IllegalStateException("Hmmm. Server does not contain overworld?");
            }
            player.changeDimension(dimension, new DungeonTeleporter(pos, this, (ServerLevel) player.level()));
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ReturnToOverWorldBlockEntity(pos, state);
    }
}