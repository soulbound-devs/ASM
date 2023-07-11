package net.vakror.soulbound.seal.seals.amplifying.sack;

import net.vakror.soulbound.seal.SealProperty;
import net.vakror.soulbound.seal.type.amplifying.IntegerSackAmplifyingSeal;
import net.vakror.soulbound.util.ArithmeticActionType;

import java.util.List;

public class ColumnUpgradeSeal extends IntegerSackAmplifyingSeal {
    public ColumnUpgradeSeal(int tier, int amount, ArithmeticActionType actionType) {
        super("column_tier", tier, amount, actionType);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("column"));
        return super.properties();
    }
}
