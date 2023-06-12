package net.vakror.asm.items.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.vakror.asm.ASMMod;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.seal.function.amplify.damage.DamageAmplifyFunction;
import net.vakror.asm.seal.tier.ISealableTier;
import net.vakror.asm.seal.type.ActivatableSeal;
import net.vakror.asm.seal.type.AttackSeal;
import net.vakror.asm.seal.type.amplifying.ItemAmplificationSeal;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class WandItem extends ActivatableSealableItem {

    public WandItem(Properties properties, ISealableTier tier) {
        super(properties, tier);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        if (TierSortingRegistry.isCorrectTierForDrops(getTier(), state)) {
            boolean hasAxing = hasSeal("axing", stack);
            boolean hasPickaxing = hasSeal("pickaxing", stack);
            boolean hasHoeing = hasSeal("hoeing", stack);

            if (hasAxing && state.is(BlockTags.MINEABLE_WITH_AXE)) {
                ASMMod.LOGGER.info("Can mine with axe");
                return true;
            }
            if (hasPickaxing && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                ASMMod.LOGGER.info("Can mine with pickaxe");
                return true;
            }
            if (hasHoeing && state.is(BlockTags.MINEABLE_WITH_HOE)) {
                ASMMod.LOGGER.info("Can mine with hoe");
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("all")
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level level = context.getLevel();
        AtomicReference<InteractionResult> result = new AtomicReference<>(null);
        player.getItemInHand(hand).getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            if (wand.getActiveSeal() != null) {
                result.set(((ActivatableSeal) wand.getActiveSeal()).useAction(context));
            }
        });
        return (result.get() == null ? InteractionResult.PASS: result.get());
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (isCorrectToolForDrops(pStack, pState)) {
            return hasSeal("mining_speed", pStack) ? this.speed * 2: this.speed;
        }
        return 1.0f;
    }

    @Override
    public float getAttackDamage() {
        AtomicReference<Float> finalDamage = new AtomicReference<>((float) 0);
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            final float[] damage = {0f};
            if (wand.getActiveSeal() != null && wand.getActiveSeal().isAttack()) {
                damage[0] = ((AttackSeal) wand.getActiveSeal()).getDamage();
            } wand.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof ItemAmplificationSeal amplificationSeal) {
                    amplificationSeal.getAmplifyFunctions().forEach((amplifyFunction -> {
                        if (amplifyFunction.isDamage()) {
                            DamageAmplifyFunction function = (DamageAmplifyFunction) amplifyFunction;
                            switch (function.getIncreaseType()) {
                                case ADD -> damage[0] += function.getAmount();
                                case SUBTRACT -> damage[0] -= function.getAmount();
                                case MULTIPLY -> damage[0] *= function.getAmount();
                                case DIVIDE -> damage[0] /= function.getAmount();
                                case POW -> damage[0] = (float) Math.pow(damage[0], function.getAmount());
                            }
                        }
                    }));
                }
            }));
            finalDamage.set(damage[0]);
        });
        return finalDamage.get();
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }
}
