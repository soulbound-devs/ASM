package net.vakror.soulbound.seal.function.amplify;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.seal.tier.SealWithAmountAndPreviousValue;

public abstract class AmplifyFunctionWithAmount extends AmplifyFunction implements SealWithAmountAndPreviousValue {
    protected final float amount;
    protected final AttributeModifier.Operation operation;

    public AmplifyFunctionWithAmount(float amount, AttributeModifier.Operation operation) {
        this.amount = amount;
        this.operation = operation;
    }

    @Override
    public float getAmount(int tier, float previousValue) {
        return operation.equals(AttributeModifier.Operation.ADDITION) ? amount + previousValue: amount * previousValue;
    }
}