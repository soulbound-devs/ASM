package net.vakror.asm.seal.type.amplifying;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.util.ArithmeticActionType;

import java.util.List;

public class SackAmplifyingSeal extends AmplifyingSeal{
    public final int amount;
    public final ArithmeticActionType actionType;
    public SackAmplifyingSeal(String id, int tier, int amount, ArithmeticActionType actionType) {
        super(id + "_" + tier);
        this.amount = amount;
        this.actionType = actionType;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("sack"));
        return super.properties();
    }
}
