package net.vakror.soulbound.seal.seals.amplifying.sack;

import net.vakror.soulbound.seal.SealProperty;
import net.vakror.soulbound.seal.type.amplifying.IntegerSackAmplifyingSeal;
import net.vakror.soulbound.util.ArithmeticActionType;

import java.util.List;

public class StackSizeUpgradeSeal extends IntegerSackAmplifyingSeal {
    public StackSizeUpgradeSeal(int tier, int amount, ArithmeticActionType actionType) {
        super("stack_size_tier", tier, amount, actionType);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("stack_size"));
        return super.properties();
    }
}
