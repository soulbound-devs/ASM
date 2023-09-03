package net.vakror.soulbound.mod.seal.seals.amplifying.sack;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.mod.seal.type.amplifying.SackAmplifyingSealWithAmount;

public class ColumnUpgradeSeal extends SackAmplifyingSealWithAmount {
    public ColumnUpgradeSeal(int tier, int amount, AttributeModifier.Operation actionType) {
        super("column_tier", tier, amount, actionType);
    }
}
