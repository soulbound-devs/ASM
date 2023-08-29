package net.vakror.soulbound.mod.items.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.TierSortingRegistry;
import net.vakror.soulbound.mod.capability.wand.ItemSealProvider;
import net.vakror.soulbound.mod.client.ModItemAnimations;
import net.vakror.soulbound.mod.model.WandBewlr;
import net.vakror.soulbound.mod.seal.ISeal;
import net.vakror.soulbound.mod.seal.function.amplify.damage.DamageAmplifyFunction;
import net.vakror.soulbound.mod.seal.seals.activatable.tool.ToolSeal;
import net.vakror.soulbound.mod.seal.tier.sealable.ISealableTier;
import net.vakror.soulbound.mod.seal.type.ActivatableSeal;
import net.vakror.soulbound.mod.seal.type.amplifying.ItemAmplifyingSeal;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ModItemAnimations.wandAnimation);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (WandBewlr.INSTANCE == null) {
                    WandBewlr.INSTANCE = new WandBewlr(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                }
                return WandBewlr.INSTANCE;
            }
        });
        super.initializeClient(consumer);
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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> map = new ImmutableMultimap.Builder<>();
            if (getActiveSeal(stack) != null) {
                ActivatableSeal seal = (ActivatableSeal) getActiveSeal(stack);
                if (seal.getAttributeModifiers() != null && !seal.getAttributeModifiers().isEmpty()) {
                    map.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "base_attack_speed_mod", -seal.swingSpeed, AttributeModifier.Operation.ADDITION));
                }
                map.putAll(seal.getAttributeModifiers());
            }
            map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier("damage_modifier_wand", getDamageFromSeals(stack), AttributeModifier.Operation.ADDITION));
            return map.build();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    public float getDamageFromSeals(ItemStack stack) {
        AtomicReference<Float> finalDamage = new AtomicReference<>((float) 0);
        stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
            final float[] damage = {0f};
            if (wand.getActiveSeal() != null) {
                damage[0] = ((ActivatableSeal) wand.getActiveSeal()).getDamage();
            } wand.getAmplifyingSeals().forEach((seal -> {
                if (seal instanceof ItemAmplifyingSeal amplificationSeal) {
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
