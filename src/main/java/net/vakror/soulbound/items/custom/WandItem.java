package net.vakror.soulbound.items.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.wand.ItemWandProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class WandItem extends DiggerItem {

    public WandItem(Properties properties) {
        super(3, -3, Tiers.DIAMOND, BlockTags.create(new ResourceLocation(SoulboundMod.MOD_ID, "none")), properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ItemWandProvider();
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        assert Minecraft.getInstance().player != null;
        if (!Minecraft.getInstance().player.level.isClientSide()) {
            boolean hasAxing = hasSeal("axing", stack);
            boolean hasPickaxing = hasSeal("pickaxing", stack);
            boolean hasHoeing = hasSeal("hoeing", stack);

            if (hasAxing && state.is(BlockTags.MINEABLE_WITH_AXE)) {
                return true;
            }
            if (hasPickaxing && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                return true;
            }
            return hasHoeing && state.is(BlockTags.MINEABLE_WITH_HOE);
        }
        return false;
    }

    @Override
    @SuppressWarnings("all")
    public InteractionResult useOn(UseOnContext pContext) {
//        if (!pContext.getLevel().isClientSide()) {
//            if (hasSeal("hoeing", pContext.getItemInHand())) {
//                int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(pContext);
//                if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
//                Level level = pContext.getLevel();
//                BlockPos blockpos = pContext.getClickedPos();
//                BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.HOE_TILL, false);
//                Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
//                if (pair == null) {
//                    return InteractionResult.PASS;
//                } else {
//                    Predicate<UseOnContext> predicate = pair.getFirst();
//                    Consumer<UseOnContext> consumer = pair.getSecond();
//                    if (predicate.test(pContext)) {
//                        Player player = pContext.getPlayer();
//                        level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
//                        if (!level.isClientSide) {
//                            consumer.accept(pContext);
//                            if (player != null) {
//                                pContext.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
//                                    p_150845_.broadcastBreakEvent(pContext.getHand());
//                                });
//                            }
//                        }
//
//                        return InteractionResult.sidedSuccess(level.isClientSide);
//                    } else {
//                        return InteractionResult.PASS;
//                    }
//                }
//            } else if (hasSeal("axing", pContext.getItemInHand())) {
//                Level level = pContext.getLevel();
//                BlockPos blockpos = pContext.getClickedPos();
//                Player player = pContext.getPlayer();
//                BlockState blockstate = level.getBlockState(blockpos);
//                Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.AXE_STRIP, false));
//                Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.AXE_SCRAPE, false));
//                Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(pContext, net.minecraftforge.common.ToolActions.AXE_WAX_OFF, false));
//                ItemStack itemstack = pContext.getItemInHand();
//                Optional<BlockState> optional3 = Optional.empty();
//                if (optional.isPresent()) {
//                    level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
//                    optional3 = optional;
//                } else if (optional1.isPresent()) {
//                    level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
//                    level.levelEvent(player, 3005, blockpos, 0);
//                    optional3 = optional1;
//                } else if (optional2.isPresent()) {
//                    level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
//                    level.levelEvent(player, 3004, blockpos, 0);
//                    optional3 = optional2;
//                }
//
//                if (optional3.isPresent()) {
//                    if (player instanceof ServerPlayer) {
//                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
//                    }
//
//                    level.setBlock(blockpos, optional3.get(), 11);
//                    if (player != null) {
//                        itemstack.hurtAndBreak(1, player, (p_150686_) -> {
//                            p_150686_.broadcastBreakEvent(pContext.getHand());
//                        });
//                    }
//
//                    return InteractionResult.sidedSuccess(level.isClientSide);
//                } else {
//                    return InteractionResult.PASS;
//                }
//            }
//        }
        return super.useOn(pContext);
    }

    public boolean hasSeal(String sealID, ItemStack stack) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
            if (wand.getSeals().contains(SealRegistry.seals.get(sealID))) {
                toReturn.set(true);
            }
        });
        return toReturn.get();
    }
}
