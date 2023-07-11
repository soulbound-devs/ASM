package net.vakror.soulbound.seal.type.amplifying;

import net.vakror.soulbound.util.ArithmeticActionType;

public class IntegerSackAmplifyingSeal extends SackAmplifyingSeal{
    public final int amount;
    public final ArithmeticActionType actionType;
    public IntegerSackAmplifyingSeal(String id, int tier, int amount, ArithmeticActionType actionType) {
        super(id + "_" + tier);
        this.amount = amount;
        this.actionType = actionType;
    }
}
