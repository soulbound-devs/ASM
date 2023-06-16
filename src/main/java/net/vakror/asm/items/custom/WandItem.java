package net.vakror.asm.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.function.amplify.damage.DamageAmplifyFunction;
import net.vakror.asm.seal.seals.activatable.tool.ToolSeal;
import net.vakror.asm.seal.tier.ISealableTier;
import net.vakror.asm.seal.type.ActivatableSeal;
import net.vakror.asm.seal.type.amplifying.ItemAmplificationSeal;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WandItem extends ActivatableSealableItem {

    public WandItem(Properties properties, ISealableTier tier) {
        super(properties, tier);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        AtomicBoolean bool = new AtomicBoolean();
        if (TierSortingRegistry.isCorrectTierForDrops(getTier(), state)) {
            List<ISeal> miningSeals = getAllSealsWithProperty("tool");
            miningSeals.forEach((seal -> {
                ToolSeal miningSeal = (ToolSeal) seal;
                if (isSealActive(miningSeal.getId(), stack) && state.is(miningSeal.mineableBlocks)) {
                    bool.set(true);
                }
            }));
        }
        return bool.get();
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> map = new ImmutableMultimap.Builder<>();
            if (getActiveSeal(stack) != null) {
                ActivatableSeal seal = (ActivatableSeal) getActiveSeal(stack);
                if (seal.getAttributeModifiers() != null && !seal.getAttributeModifiers().isEmpty()) {
                    map.putAll(seal.getAttributeModifiers());
                }
            }
            return map.build();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    public float getAttackDamage() {
        AtomicReference<Float> finalDamage = new AtomicReference<>((float) 0);
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            final float[] damage = {0f};
            if (wand.getActiveSeal() != null) {
                damage[0] = ((ActivatableSeal) wand.getActiveSeal()).getDamage();
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
}
