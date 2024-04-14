package net.vakror.soulbound.compat.dungeon.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachment;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import net.vakror.soulbound.compat.dungeon.blocks.entity.ModDungeonBlockEntities;
import net.vakror.soulbound.compat.dungeon.blocks.entity.ReturnToOverWorldBlockEntity;
import net.vakror.soulbound.compat.dungeon.dimension.DungeonToOverworldTeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("deprecation")
public class ReturnToOverworldBlock extends BaseEntityBlock {
    public ReturnToOverworldBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ReturnToOverworldBlock::new);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    //TODO: update
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            /*
            player teleportation
            */
            ServerLevel dimension = level.getServer().overworld();
            player.setPortalCooldown();
            ReturnToOverWorldBlockEntity entity = ((ReturnToOverWorldBlockEntity) level.getBlockEntity(pos));
            AtomicReference<InteractionResult> result = new AtomicReference<>(InteractionResult.PASS);
            DungeonAttachment dungeon = player.level().getData(DungeonAttachments.DUNGEON_ATTACHMENT);
            if (!dungeon.getSetup().canExit(level)) {
                assert entity != null;
                if (entity.getCannotExitMessageDelay() <= 0) {
                    player.sendSystemMessage(Component.literal(dungeon.getDungeon().getCannotExitMessage()));
                    entity.setCannotExitMessageDelay(200);
                }
                result.set(InteractionResult.FAIL);
            } else {
                player.changeDimension(dimension, new DungeonToOverworldTeleporter((ServerLevel) player.level()));
                result.set(InteractionResult.SUCCESS);
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ReturnToOverWorldBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModDungeonBlockEntities.RETURN_TO_OVERWORLD_BLOCK_ENTITY.get(), ReturnToOverWorldBlockEntity::tick);
    }
}