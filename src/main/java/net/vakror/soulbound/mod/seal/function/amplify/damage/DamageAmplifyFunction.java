package net.vakror.soulbound.mod.seal.function.amplify.damage;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.mod.seal.function.amplify.AmplifyFunctionWithAmountAndCap;

public class DamageAmplifyFunction extends AmplifyFunctionWithAmountAndCap {

    public DamageAmplifyFunction(float amount, AttributeModifier.Operation operation, float cap) {
        super(amount, operation, cap);
    }
}