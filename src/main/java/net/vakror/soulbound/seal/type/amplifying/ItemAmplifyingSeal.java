package net.vakror.soulbound.seal.type.amplifying;

import net.vakror.soulbound.seal.function.amplify.AmplifyFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemAmplifyingSeal extends AmplifyingSeal {
    private final List<AmplifyFunction> amplifyFunctions;

    public ItemAmplifyingSeal(String id) {
        super(id);
        this.amplifyFunctions = new ArrayList<>();
    }

    public void addAmplifyFunction(AmplifyFunction function) {
        amplifyFunctions.add(function);
    }

    public List<AmplifyFunction> getAmplifyFunctions() {
        return amplifyFunctions;
    }
}
