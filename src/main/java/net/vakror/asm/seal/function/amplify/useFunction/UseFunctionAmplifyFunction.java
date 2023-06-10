package net.vakror.asm.seal.function.amplify.useFunction;

import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.function.amplify.AmplifyFunction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UseFunctionAmplifyFunction extends AmplifyFunction {

    public UseFunctionAmplifyFunction(@Nullable List<ISeal> affectedSeals) {
        super(affectedSeals);
    }
}
