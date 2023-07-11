package net.vakror.soulbound.seal.function.amplify;

import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.function.amplify.damage.DamageAmplifyFunction;
import net.vakror.soulbound.seal.function.amplify.damage.aoe.AoeAmplifyFunction;

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