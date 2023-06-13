package net.vakror.asm.seal.seals.amplifying;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.BaseSeal;
import net.vakror.asm.util.ArithmeticActionType;

import java.util.List;

public class StackSizeUpgradeSeal extends BaseSeal {
    public final int amount;
    public final ArithmeticActionType actionType;
    public StackSizeUpgradeSeal(int tier, int amount, ArithmeticActionType actionType) {
        super("stack_size_tier_" + tier, false);
        this.amount = amount;
        this.actionType = actionType;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("sack"));
        properties.add(new SealProperty("stack_size"));
        return super.properties();
    }
}
