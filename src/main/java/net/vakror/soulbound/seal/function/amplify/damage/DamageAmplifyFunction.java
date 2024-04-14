package net.vakror.soulbound.seal.function.amplify.damage;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.seal.function.amplify.AmplifyFunctionWithAmountAndCap;

public class DamageAmplifyFunction extends AmplifyFunctionWithAmountAndCap {

    public DamageAmplifyFunction(float amount, AttributeModifier.Operation operation, float cap) {
        super(amount, operation, cap);
    }
}