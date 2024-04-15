package net.vakror.soulbound.seal.type.amplifying;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.seal.tier.SealWithAmountAndPreviousValue;

public class SackAmplifyingSealWithAmount extends SackAmplifyingSeal implements SealWithAmountAndPreviousValue {
    public final int amount;
    public final AttributeModifier.Operation actionType;
    public SackAmplifyingSealWithAmount(String id, int tier, int amount, AttributeModifier.Operation actionType) {
        super(new ResourceLocation(SoulboundMod.MOD_ID,id + "_" + tier));
        this.amount = amount;
        this.actionType = actionType;
    }

    @Override
    public float getAmount(int tier, float previousValue) {
        return actionType.equals(AttributeModifier.Operation.ADDITION) ? previousValue + amount: previousValue * amount;
    }
}
