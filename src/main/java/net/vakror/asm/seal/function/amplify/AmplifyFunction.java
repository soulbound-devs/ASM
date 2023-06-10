package net.vakror.asm.seal.function.amplify;

import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.SealRegistry;
import net.vakror.asm.seal.function.amplify.damage.DamageAmplifyFunction;
import net.vakror.asm.seal.function.amplify.damage.aoe.AoeAmplifyFunction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class AmplifyFunction {
    protected List<ISeal> affectedSeals;

    public AmplifyFunction(@Nullable List<ISeal> affectedSeals) {
        this.affectedSeals = Objects.requireNonNullElseGet(affectedSeals, () -> (List<ISeal>) SealRegistry.allSeals.values());
    }

    public List<ISeal> getAffectedSeals() {
        return affectedSeals;
    }

    public void setAffectedSeals(List<ISeal> affectedSeals) {
        this.affectedSeals = affectedSeals;
    }

    public boolean isDamage() {
        return this instanceof DamageAmplifyFunction && !isAoe();
    }

    public boolean isAoe() {
        return this instanceof AoeAmplifyFunction;
    }
}