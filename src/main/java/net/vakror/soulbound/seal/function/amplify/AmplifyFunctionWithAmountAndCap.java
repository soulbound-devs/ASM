package net.vakror.soulbound.seal.function.amplify;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.seal.tier.SealWithAmountAndPreviousValue;

public abstract class AmplifyFunctionWithAmountAndCap extends AmplifyFunctionWithAmount implements SealWithAmountAndPreviousValue {

    private final float cap;

    public AmplifyFunctionWithAmountAndCap(float amount, AttributeModifier.Operation operation, float cap) {
        super(amount, operation);
        this.cap = cap;
    }

    @Override
    public float getAmount(int tier, float previousValue) {
        return Math.min(cap, super.getAmount(tier, previousValue));
    }
}