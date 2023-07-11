package net.vakror.soulbound.seal.function.amplify.damage.aoe;

import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.util.ArithmeticActionType;
import net.vakror.soulbound.seal.function.amplify.damage.DamageAmplifyFunction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AoeAmplifyFunction extends DamageAmplifyFunction {
    protected final AoeAmplifyType amplifyType;

    public AoeAmplifyFunction(ArithmeticActionType action, int damage, @Nullable Integer cap, AoeAmplifyType amplifyType, @Nullable List<ISeal> affectedSeals) {
        super(action, damage, cap, affectedSeals);
        this.amplifyType = amplifyType;
    }

    public AoeAmplifyType getAmplifyType() {
        return amplifyType;
    }

    public enum AoeAmplifyType {
        DAMAGE,
        MAX_MOBS,
        RANGE
    }
}
