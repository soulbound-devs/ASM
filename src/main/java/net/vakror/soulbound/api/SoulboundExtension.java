package net.vakror.soulbound.api;

import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;

public abstract class SoulboundExtension {
    /**
     * called during the register models phase when models are ready to be registered
     * @param context the context that models will be registered in. <b>Models MUST be registered using this</b>
     */
    public abstract void registerModels(ModelRegistrationContext context);
    public abstract void registerSeals(SealRegistrationContext context);

    /**
     *
     * @return the type(s) of seal registration context types to use for registration, non-default ones can be registered in {@link SoulboundApi#addRegistrationContext(SealRegistrationContext) addRegistrationContext}. Uses default contexts: {@link SealRegistrationContext}. Multiple entries are separated with ", ".
     */
    public String getSealContextTypes() {
        return "default";
    };

    /**
     *
     * @return the type(s) of seal registration context types to use for registration, non-default ones can be registered in {@link SoulboundApi#addRegistrationContext(ModelRegistrationContext) addRegistrationContext}. Uses default contexts: {@link ModelRegistrationContext}. Multiple entries are separated with ", ".
     */
    public String getModelContextTypes() {
        return "default";
    };
}
