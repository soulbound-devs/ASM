package net.vakror.soulbound.seal.function.amplify.damage;

import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.function.amplify.AmplifyFunction;
import net.vakror.soulbound.util.ArithmeticActionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DamageAmplifyFunction extends AmplifyFunction {
    protected final ArithmeticActionType arithmeticActionType;
    protected final int amount;
    protected final int cap;

    public DamageAmplifyFunction(ArithmeticActionType action, int damage, @Nullable Integer cap, @Nullable List<ISeal> affectedSeals) {
        super(affectedSeals);
        this.arithmeticActionType = action;
        this.amount = damage;
        this.cap = Objects.requireNonNullElse(cap, Integer.MAX_VALUE);
    }

    public ArithmeticActionType getIncreaseType() {
        return arithmeticActionType;
    }

    public int getAmount() {
        return amount;
    }

    public int getCap() {
        return cap;
    }
}
