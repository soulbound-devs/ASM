package net.vakror.asm.seal.type.amplifying;

import net.vakror.asm.seal.function.amplify.AmplifyFunction;

import java.util.List;

public abstract class ItemAmplificationSeal extends AmplifyingSeal {
    private final List<AmplifyFunction> amplifyFunctions;
    private final int priority;

    public ItemAmplificationSeal(String id, List<AmplifyFunction> amplifyFunctions, int priority) {
        super(id);
        this.amplifyFunctions = amplifyFunctions;
        this.priority = priority;
    }

    public List<AmplifyFunction> getAmplifyFunctions() {
        return amplifyFunctions;
    }

    public int getPriority() {
        return priority;
    }
}
