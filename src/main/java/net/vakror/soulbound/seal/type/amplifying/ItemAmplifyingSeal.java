package net.vakror.soulbound.seal.type.amplifying;

import net.vakror.soulbound.seal.function.amplify.AmplifyFunction;

import java.util.List;

public abstract class ItemAmplifyingSeal extends AmplifyingSeal {
    private final List<AmplifyFunction> amplifyFunctions;

    public ItemAmplifyingSeal(String id, List<AmplifyFunction> amplifyFunctions, int priority) {
        super(id);
        this.amplifyFunctions = amplifyFunctions;
    }

    public List<AmplifyFunction> getAmplifyFunctions() {
        return amplifyFunctions;
    }
}
