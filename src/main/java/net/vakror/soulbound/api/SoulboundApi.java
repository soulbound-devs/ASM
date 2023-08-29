package net.vakror.soulbound.api;

import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoulboundApi {
    private static final List<SoulboundExtension> EXTENSIONS = new ArrayList<>();
    private static final List<SealRegistrationContext> SEAL_CONTEXT = new ArrayList<>();
    private static final List<ModelRegistrationContext> MODEL_CONTEXT = new ArrayList<>();

    /**
     * <b>MUST</b> be called in mod constructor before any registration is done
     * @param extension the extension instance you want to register
     */
    public static <T extends SoulboundExtension> void registerExtension(T extension) {
        EXTENSIONS.add(extension);
    }

    public static void addDefaultContexts() {
        addRegistrationContext(new SealRegistrationContext());
        addRegistrationContext(new ModelRegistrationContext());
    }

    /**
     * used to add a new model context for whatever advanced things you need to do
     * @param context the context to add
     */
    protected static <T extends ModelRegistrationContext> void addRegistrationContext(T context) {
        MODEL_CONTEXT.add(context);
    }

    /**
     * used to add a new seal context for whatever advanced things you need to do
     * @param context the context to add
     */
    protected static <T extends SealRegistrationContext> void addRegistrationContext(T context) {
        SEAL_CONTEXT.add(context);
    }

    /**
     * <b><h2><i>INTERNAL USE ONLY! DO NOT CALL</i></h2></b>
     */
    @ApiStatus.Internal
    public static void onSealsRegister() {
        SEAL_CONTEXT.forEach((sealRegistrationContext -> {
            EXTENSIONS.forEach((soulboundExtension -> {
                if (Arrays.stream(soulboundExtension.getModelContextTypes().split(", ")).anyMatch((s) -> s.equals(sealRegistrationContext.getName()))) {
                    soulboundExtension.registerSeals(sealRegistrationContext);
                }
            }));
        }));
    }

    /**
     * <b><h2><i>INTERNAL USE ONLY! DO NOT CALL</i></h2></b>
     */
    @ApiStatus.Internal
    public static void onModelsRegister() {
        MODEL_CONTEXT.forEach((modelRegistrationContext -> {
            EXTENSIONS.forEach((soulboundExtension -> {
                if (Arrays.stream(soulboundExtension.getModelContextTypes().split(", ")).anyMatch((s) -> s.equals(modelRegistrationContext.getName()))) {
                    soulboundExtension.registerModels(modelRegistrationContext);
                }
            }));
        }));
    }
}
