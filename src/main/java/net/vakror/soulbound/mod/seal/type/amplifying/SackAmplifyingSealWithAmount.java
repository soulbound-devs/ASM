package net.vakror.soulbound.mod.seal.type.amplifying;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.mod.seal.tier.SealWithAmountAndPreviousValue;

public class SackAmplifyingSealWithAmount extends SackAmplifyingSeal implements SealWithAmountAndPreviousValue {
    public final int amount;
    public final AttributeModifier.Operation actionType;
    public SackAmplifyingSealWithAmount(String id, int tier, int amount, AttributeModifier.Operation actionType) {
        super(id + "_" + tier);
        this.amount = amount;
        this.actionType = actionType;
    }

    @Override
    public float getAmount(int tier, float previousValue) {
        return actionType.equals(AttributeModifier.Operation.ADDITION) ? previousValue + amount: previousValue * amount;
    }
}
