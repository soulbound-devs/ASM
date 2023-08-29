package net.vakror.soulbound.mod.seal.seals.amplifying.sack;

import net.vakror.soulbound.mod.seal.SealProperty;
import net.vakror.soulbound.mod.seal.type.amplifying.IntegerSackAmplifyingSeal;
import net.vakror.soulbound.mod.util.ArithmeticActionType;

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
