package net.vakror.soulbound.items.custom;

import com.google.common.collect.Maps;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class BarkItem extends Item {
    public static final ItemColor COLOR = (pStack, pTintIndex) -> {
        float[] barkColor = ((BarkItem) pStack.getItem()).barkColor.getTextureDiffuseColors();
        return FastColor.ARGB32.color(255, (int) (barkColor[0] * 255), (int) (barkColor[1] * 255), (int) (barkColor[2] * 255));
    };

    public static final Map<DyeColor, BarkItem> ITEM_BY_COLOR = Maps.newEnumMap(DyeColor.class);
    private final DyeColor barkColor;

    public BarkItem(DyeColor pDyeColor, Item.Properties pProperties) {
        super(pProperties);
        this.barkColor = pDyeColor;
        ITEM_BY_COLOR.put(pDyeColor, this);
    }

    /**
     * Try interacting with given entity. Return {@code InteractionResult.PASS} if nothing should happen.
     */
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pTarget, InteractionHand pHand) {
        if (pTarget instanceof Sheep sheep) {
            if (sheep.isAlive() && !sheep.isSheared() && sheep.getColor() != this.barkColor) {
                sheep.level.playSound(pPlayer, sheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!pPlayer.level.isClientSide) {
                    sheep.setColor(this.barkColor);
                    pStack.shrink(1);
                }

                return InteractionResult.sidedSuccess(pPlayer.level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    public DyeColor getBarkColor() {
        return this.barkColor;
    }

    public static BarkItem byColor(DyeColor pColor) {
        return ITEM_BY_COLOR.get(pColor);
    }
}