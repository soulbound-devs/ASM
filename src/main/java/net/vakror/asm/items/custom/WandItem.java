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
import net.vakror.asm.capability.wand.IWandTier;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.seal.function.amplify.AmplifyFunction;
import net.vakror.asm.seal.function.amplify.damage.DamageAmplifyFunction;
import net.vakror.asm.seal.type.ActivatableSeal;
import net.vakror.asm.seal.type.AttackSeal;
import net.vakror.asm.seal.type.amplifying.ItemAmplificationSeal;
import net.vakror.asm.util.MapUtil;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class WandItem extends ActivatableSealableItem {

    public WandItem(Properties properties, IWandTier tier) {
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
        AtomicReference<Float> damage = new AtomicReference<>((float) 0);
        final Map<DamageAmplifyFunction, Integer> damageMap = new LinkedHashMap<>();
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            if (wand.getActiveSeal() != null && wand.getActiveSeal().isAttack()) {
                damage.set(((AttackSeal) wand.getActiveSeal()).getDamage());
            }
            wand.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof ItemAmplificationSeal amplificationSeal) {
                    List<AmplifyFunction> damageFunctions = amplificationSeal.getAmplifyFunctions().stream().filter((AmplifyFunction::isDamage)).toList();
                    damageFunctions.forEach((damageAmplifyFunction -> {
                        damageMap.put((DamageAmplifyFunction) damageAmplifyFunction, amplificationSeal.getPriority());
                    }));
                }
            }));
        });
        Map<DamageAmplifyFunction, Integer> sortedDamageMap = MapUtil.sortByValue(damageMap);
        sortedDamageMap.keySet().forEach((damageAmplifyFunction -> {
            float damageAmount = damageAmplifyFunction.getAmount();
            switch (damageAmplifyFunction.getIncreaseType()) {
                case ADD -> damage.set(damage.get() + damageAmount);
                case MULTIPLY -> damage.set(damage.get() * damageAmount);
                case SUBTRACT -> damage.set(damage.get() - damageAmount);
                case DIVIDE -> damage.set(damage.get() / damageAmount);
                case POW -> damage.set((float) Math.pow(damage.get(), damageAmount));
            }
        }));
        return damage.get();
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }
}
