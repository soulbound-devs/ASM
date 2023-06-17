package net.vakror.asm.seal.seals.amplifying.sack;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.amplifying.SackAmplifyingSeal;
import net.vakror.asm.util.ArithmeticActionType;

import java.util.List;

public class ColumnUpgradeSeal extends SackAmplifyingSeal {
    public ColumnUpgradeSeal(int tier, int amount, ArithmeticActionType actionType) {
        super("column_tier_", tier, amount, actionType);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("column"));
        return super.properties();
    }
}
