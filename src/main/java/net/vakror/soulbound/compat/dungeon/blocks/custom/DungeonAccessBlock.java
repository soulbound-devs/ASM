package net.vakror.soulbound.compat.dungeon.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.commoble.infiniverse.api.InfiniverseAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.DungeonSaveData;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachment;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachments;
import net.vakror.soulbound.compat.dungeon.blocks.entity.DungeonAccessBlockEntity;
import net.vakror.soulbound.compat.dungeon.dimension.Dimensions;
import net.vakror.soulbound.compat.dungeon.dimension.OverworldToDungeonTeleporter;
import net.vakror.soulbound.compat.dungeon.items.ModDungeonItems;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(DungeonAccessBlock::new);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (canUnlock(player, level, state, hand)) {
                return unlock(level, pos, state);
            } else if (canTeleport(level, state)) {
                return teleportPlayer(level, pos, player);
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult teleportPlayer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof DungeonAccessBlockEntity blockEntity) {
            if (blockEntity.getDimensionUUID() == null) {
                blockEntity.setDimensionUUID(UUID.randomUUID());
                blockEntity.setChanged();
            }
            ServerLevel serverLevel = (ServerLevel) level;
            boolean levelExists = doesLevelExist(level.getServer(), blockEntity.getDimensionUUID());
            final boolean[] canEnter = new boolean[]{true};
            ResourceLocation dimensionId = new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_" + blockEntity.getDimensionUUID().toString());
            ServerLevel dimension = createDimension(dimensionId, serverLevel);
            DungeonSaveData.DungeonLevelsListSaveData.INSTANCE.loadedDungeonLevels.add(dimensionId);
            DungeonSaveData.DungeonLevelsListSaveData.INSTANCE.setDirty();
            DungeonAttachment dungeonLevel = dimension.getData(DungeonAttachments.DUNGEON_ATTACHMENT);
            dungeonLevel.setReturnPos(player.position());
            if (canTeleport(level, player, dimension, blockEntity.getDimensionUUID())) {
                player.setPortalCooldown();
                Objects.requireNonNull(player.changeDimension(dimension, new OverworldToDungeonTeleporter()));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static ServerLevel createDimension(ResourceLocation dimensionId, ServerLevel serverLevel) {
        return InfiniverseAPI.get().getOrCreateLevel(serverLevel.getServer(),
                ResourceKey.create(Registries.DIMENSION, dimensionId),
                () -> new LevelStem(
                        serverLevel.registryAccess()
                                .registryOrThrow(Registries.DIMENSION_TYPE)
                                .getHolderOrThrow(Dimensions.DUNGEON_TYPE),
                        new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(), serverLevel.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.PLAINS), List.of()))));
    }

    private boolean canUnlock(Player player, Level level, BlockState state, InteractionHand hand) {
        return !level.isClientSide && player.getItemInHand(hand).getItem().equals(ModDungeonItems.KEY.get()) && state.getValue(LOCKED);
    }

    private boolean canTeleport(Level level, BlockState state) {
        return !level.isClientSide && !state.getValue(LOCKED);
    }

    private InteractionResult unlock(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(LOCKED, false), 35);
        return InteractionResult.CONSUME;
    }

    public static boolean doesLevelExist(MinecraftServer server, UUID uuid) {
        ResourceLocation id = new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_" + uuid.toString());
        ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, id);

        @SuppressWarnings("deprecation") // forgeGetWorldMap is deprecated because it's a forge-internal-use-only method
        final Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();
        // if the level already exists, return true
        final ServerLevel existingLevel = map.get(levelKey);
        return existingLevel != null;
    }

    private boolean canTeleport(Level level, Player player, ServerLevel dimension, UUID dimensionUUID) {
        if (!dimension.players().isEmpty()) {
            SoulboundMod.LOGGER.info(player.getDisplayName() + "Cannot Enter" + "Dungeon, as " + dimension.players().get(0) + "Is already in it");
        }
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
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
