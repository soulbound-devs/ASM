package net.vakror.soulbound.seal.seals.amplifying.sack;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.seal.type.amplifying.SackAmplifyingSealWithAmount;

public class RowUpgradeSeal extends SackAmplifyingSealWithAmount {
    public RowUpgradeSeal(int tier, int amount, AttributeModifier.Operation actionType) {
        super("row_tier", tier, amount, actionType);
    }
}
