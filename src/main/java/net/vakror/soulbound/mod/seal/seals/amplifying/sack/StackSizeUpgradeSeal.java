package net.vakror.soulbound.mod.seal.seals.amplifying.sack;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.mod.seal.type.amplifying.SackAmplifyingSealWithAmount;

public class StackSizeUpgradeSeal extends SackAmplifyingSealWithAmount {
    public StackSizeUpgradeSeal(int tier, int amount, AttributeModifier.Operation actionType) {
        super("stack_size_tier", tier, amount, actionType);
    }
}
