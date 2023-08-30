package net.vakror.soulbound.api;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.DeferredRegister;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.model.models.ActiveSealModels;
import net.vakror.soulbound.mod.model.models.WandModels;
import net.vakror.soulbound.mod.seal.SealRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * This is the main API class to soulbound that all extensions are stored in and all direct API calls are made through
 */
public class SoulboundApi {
    private static final List<ISoulboundExtension> EXTENSIONS = new ArrayList<>();
    private static final List<SealRegistrationContext> SEAL_CONTEXT = new ArrayList<>();
    private static final List<ModelRegistrationContext> MODEL_CONTEXT = new ArrayList<>();

    /**
     * Registers a soulbound extension manually; this method is not recommended, instead opt for the annotation method
     * <b>MUST</b> be called in mod constructor before any registration is done
     * @param extension the extension instance you want to register
     */
    @Deprecated(since = "1.5.5-ALPHA")
    public static <T extends ISoulboundExtension> void registerExtension(T extension) {
        EXTENSIONS.add(extension);
    }

    /**
     * Used to add default contexts. <b>DO NOT CALL!</b>
     */
    @ApiStatus.Internal
    public static void addDefaultContexts() {
        addRegistrationContext(new SealRegistrationContext());
        addRegistrationContext(new ModelRegistrationContext());
    }

    /**
     * used to add a new model context for whatever advanced things you need to do
     * <br>
     * This must be called before {@link net.minecraftforge.registries.RegisterEvent registration} has fired as that is when seals are registered and!
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
                if (Arrays.stream(soulboundExtension.getSealContextTypes().split(", ")).anyMatch((s) -> s.equals(sealRegistrationContext.getName()))) {
                    soulboundExtension.registerSeals(sealRegistrationContext);
                }
            }));
        }));
        onSealRegistrationDone();
        SealRegistry.doneRegistering();
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
        onModelRegistrationDone();
        ActiveSealModels.MODELS = ImmutableMap.copyOf(ActiveSealModels.getModels());
        WandModels.MODELS = ImmutableMap.copyOf(WandModels.getModels());
    }

    /**
     * This is called after seal registration is done and before the seal map is made unmodifiable. DO NOT CALL. This calls {@link ISoulboundExtension#onSealRegistrationDone() onSealRegistrationDone}
     */
    public static void onSealRegistrationDone() {
        for (DeferredRegister<Item> value : SealRegistrationContext.REGISTRIES.values()) {
            value.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }

    /**
     * This is called after model registration is done and before the model map is made unmodifiable. DO NOT CALL. This calls {@link ISoulboundExtension#onModelRegistrationDone() onModelRegistrationDone}
     */
    @ApiStatus.Internal
    public static void onModelRegistrationDone() {

    }

    public static List<ISoulboundExtension> findAllAnnotatedSoulboundExtensions() {
        return findInstances(Extension.class, ISoulboundExtension.class);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T> List<T> findInstances(Class<?> annotationClass, Class<T> instanceClass) {
        Type annotationType = Type.getType(annotationClass);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<String> pluginClassNames = new LinkedHashSet<>();
        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (Objects.equals(a.annotationType(), annotationType)) {
                    String memberName = a.memberName();
                    pluginClassNames.add(memberName);
                }
            }
        }
        List<T> instances = new ArrayList<>();
        for (String className : pluginClassNames) {
            try {
                Class<?> asmClass = Class.forName(className);
                Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
                Constructor<? extends T> constructor = asmInstanceClass.getDeclaredConstructor();
                T instance = constructor.newInstance();
                instances.add(instance);
            } catch (ReflectiveOperationException | LinkageError e) {
                SoulboundMod.LOGGER.error("Failed to load: {}", className, e);
            }
        }
        return instances;
    }

    public static void registerAnnotatedExtensions() {
        findAllAnnotatedSoulboundExtensions().forEach((SoulboundApi::registerExtension));
    }
}
