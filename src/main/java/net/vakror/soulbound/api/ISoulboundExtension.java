package net.vakror.soulbound.api;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;

/**
 * the base class that all extensions inherit from. This is where all registers are received
 * if an automatically registered extensions (auto-registered by annotating with {@link Extension}) has a constructor with more than zero arguments, it must declare another one with zero arguments or be manually registered by {@link SoulboundApi#registerExtension(ISoulboundExtension) registerExtension}
 */
public interface ISoulboundExtension {
    /**
     * called during the register models phase when models are ready to be registered
     * @param context the context that models will be registered in. <b>Models MUST be registered using this</b>
     */
    void registerModels(ModelRegistrationContext context);
    void registerSeals(SealRegistrationContext context);

    /**
     *
     * @return the type(s) of seal registration context types to use for registration, non-default ones can be registered in {@link SoulboundApi#addRegistrationContext(SealRegistrationContext) addRegistrationContext}. Uses default contexts: {@link SealRegistrationContext}. Multiple entries are separated with ", ".
     */
    default String getSealContextTypes() {
        return "default";
    };

    /**
     *
     * @return the type(s) of seal registration context types to use for registration, non-default ones can be registered in {@link SoulboundApi#addRegistrationContext(ModelRegistrationContext) addRegistrationContext}. Uses default contexts: {@link ModelRegistrationContext}. Multiple entries are separated with ", ".
     */
    default String getModelContextTypes() {
        return "default";
    };

    /**
     *
     * @return The id of this extension.<b>MAKE IT UNIQUE!</b>. This will be used for overriding and removing extensions, as well as for debug logs
     */
    ResourceLocation getExtensionName();

    /**
     * This is called when seal registration is done but before the seal and seal item maps are made unmodifiable. Use this to do other stuff that has to run after all seals have been registered but before the registration step is done
     */
    default void onSealRegistrationDone() {}

    /**
     * This is called when model registration is done but before the model map is made unmodifiable. Use this to do other stuff that has to run after all models have been registered but before the registration step is done
     */
    default void onModelRegistrationDone() {}
}
