package net.vakror.asm.seal.seals.amplifying.sack;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.amplifying.IntegerSackAmplifyingSeal;
import net.vakror.asm.util.ArithmeticActionType;

import java.util.List;

public class RowUpgradeSeal extends IntegerSackAmplifyingSeal {
    public RowUpgradeSeal(int tier, int amount, ArithmeticActionType actionType) {
        super("row_tier", tier, amount, actionType);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("row"));
        return super.properties();
    }
}
