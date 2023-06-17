package net.vakror.asm.seal.seals.amplifying.sack;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.amplifying.SackAmplifyingSeal;
import net.vakror.asm.util.ArithmeticActionType;

import java.util.List;

public class RowUpgradeSeal extends SackAmplifyingSeal {
    public RowUpgradeSeal(int tier, int amount, ArithmeticActionType actionType) {
        super("column_tier_", tier, amount, actionType);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("row"));
        return super.properties();
    }
}
