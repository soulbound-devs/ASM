package net.vakror.soulbound.api;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.mod.seal.ISeal;

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

    /**
     * This is where you can execute custom logic after a seal has been added
     * <b>DO NOT USE {@link SoulboundApi#onRegisterSeal}, USE THIS INSTEAD</b>
     * This method is sorted by priority of your extension
     * @param seal
     */
    default void onRegisterSeal(ISeal seal) {}

    /**
     * Called when a model
     * @param model the location of the model that was registered
     * <b>DO NOT USE {@link SoulboundApi#onRegisterModel}, USE THIS INSTEAD</b>
     * @param isSpellModel whether the model is a spell model
     */
    default void onRegisterModel(ResourceLocation model, boolean isSpellModel) {}

    /**
     * Use this for whatever you need to do right after the extension has been registered
     * Runs only once
     */
    default void onRegistered() {}

    /**
     * <b>Note:</b> This is affected by priority, if any one of these calls return false, this method will not be called for extensions of lower priority or those that are registered later
     * If needed, register your extension with a higher priority so that you can precede any others who may cancel your registration
     * Not recommended to override
     * <br>
     * @param seal the seal that is being registered
     * @return whether the seal will be registered
     */
    default boolean canRegisterSeal(ISeal seal) {return true;}

    /**
     * <b>Note:</b> This is affected by priority, if any one of these calls return false, this method will not be called for extensions of lower priority or those that are registered later
     * If needed, register your extension with a higher priority so that you can precede any others who may cancel your registration
     * Not recommended to override
     * <br>
     * @param name the name of the model that is being registered
     * @param model the resourceLocation of the spell model that is being registered
     * @return whether the spell model will be registered
     */
    default boolean canRegisterSpellModel(String name, ResourceLocation model) {return true;}

    /**
     * <b>Note:</b> This is affected by priority, if any one of these calls return false, this method will not be called for extensions of lower priority or those that are registered later
     * If needed, register your extension with a higher priority so that you can precede any others who may cancel your registration
     * Not recommended to override
     * <br>
     * @param name the name of the model that is being registered
     * @param model the resourceLocation of the wand model that is being registered
     * @return whether the wand model will be registered
     */
    default boolean canRegisterWandModel(String name, ResourceLocation model) {return true;}

    /**
     * Gets the priority
     * @return how early everything in the extension is loaded, the higher the value, the earlier it will be loaded
     */
    default int priority() {
        return 0;
    }
}
