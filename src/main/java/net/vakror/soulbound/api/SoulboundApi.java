package net.vakror.soulbound.api;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.model.models.ActiveSealModels;
import net.vakror.soulbound.mod.model.models.WandModels;
import net.vakror.soulbound.mod.seal.ISeal;
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
     * <b>MUST</b> be called while items are being registered in {@link RegisterEvent}
     * If this method is used, you cannot specify a priority like you can in {@link Extension @Extension}
     * This is one of the many reasons using {@link Extension @Extension} is preferred
     * @param extension the extension instance you want to register
     */
    public static <T extends ISoulboundExtension> void registerExtension(T extension) {
        EXTENSIONS.add(extension);
    }

    /**
     * Used to add default contexts. <b>DO NOT CALL!</b>
     */
    @ApiStatus.Internal
    public static void addDefaultContexts() {
        SoulboundMod.LOGGER.info("Registering Default Soulbound Contexts");
        Stopwatch stopwatch = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Registering Default Seal Registration Context");
        Stopwatch stopwatch1 = Stopwatch.createStarted();
        addRegistrationContext(new SealRegistrationContext());
        SoulboundMod.LOGGER.info("Finished Registering Default Seal Registration Context, \033[0;31mTook {}\033[0;0m", stopwatch1);
        SoulboundMod.LOGGER.info("Registering Default Model Registration Context");
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        addRegistrationContext(new ModelRegistrationContext());
        SoulboundMod.LOGGER.info("Finished Registering Default Seal Registration Context, \033[0;31mTook {}\033[0;0m", stopwatch2);
        SoulboundMod.LOGGER.info("Finished Registering Default Soulbound Context, \033[0;31mTook {}\033[0;0m", stopwatch);
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
        SoulboundMod.LOGGER.info("Registering Seals");
        Stopwatch stopwatch = Stopwatch.createStarted();
        SEAL_CONTEXT.forEach((sealRegistrationContext -> {
            SoulboundMod.LOGGER.info("Starting Seal Registration Using Context {}", sealRegistrationContext.getContextName());
            Stopwatch stopwatch1 = Stopwatch.createStarted();
            EXTENSIONS.forEach((soulboundExtension -> {
                SoulboundMod.LOGGER.info("Starting Registration Of Seals using Extension {} and Context {}", soulboundExtension.getExtensionName(), sealRegistrationContext.getContextName());
                Stopwatch stopwatch2 = Stopwatch.createStarted();
                if (Arrays.stream(soulboundExtension.getSealContextTypes().split(", ")).anyMatch((s) -> s.equals(sealRegistrationContext.getContextName()))) {
                    soulboundExtension.registerSeals(sealRegistrationContext);
                }
                SoulboundMod.LOGGER.info("Finished Registration Of Seals using Extension {} and Context {}, \033[0;31mTook {}\033[0;0m", soulboundExtension.getExtensionName(), sealRegistrationContext.getContextName(), stopwatch2);
            }));
            SoulboundMod.LOGGER.info("Finished Seal Registration using Context {}, \033[0;31mTook {}\033[0;0m", sealRegistrationContext.getContextName(), stopwatch1);
        }));
        onSealRegistrationDone();
        SealRegistry.doneRegistering();
        SoulboundMod.LOGGER.info("Finished Registering Seals, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    /**
     * <b><h2><i>INTERNAL USE ONLY! DO NOT CALL</i></h2></b>
     */
    @ApiStatus.Internal
    public static void onModelsRegister() {
        SoulboundMod.LOGGER.info("Registering Models");
        Stopwatch stopwatch = Stopwatch.createStarted();
        MODEL_CONTEXT.forEach((modelRegistrationContext -> {
            SoulboundMod.LOGGER.info("Starting Model Registration Using Context {}", modelRegistrationContext.getContextName());
            Stopwatch stopwatch1 = Stopwatch.createStarted();
            EXTENSIONS.forEach((soulboundExtension -> {
                SoulboundMod.LOGGER.info("Starting Registration Of Models using Extension {} and Context {}", soulboundExtension.getExtensionName(), modelRegistrationContext.getContextName());
                Stopwatch stopwatch2 = Stopwatch.createStarted();
                if (Arrays.stream(soulboundExtension.getModelContextTypes().split(", ")).anyMatch((s) -> s.equals(modelRegistrationContext.getContextName()))) {
                    soulboundExtension.registerModels(modelRegistrationContext);
                }
                SoulboundMod.LOGGER.info("Finished Registration Of Models using Extension {} and Context {}, \033[0;31mTook {}\033[0;0m", soulboundExtension.getExtensionName(), modelRegistrationContext.getContextName(), stopwatch2);
            }));
            SoulboundMod.LOGGER.info("Finished Model Registration using Context {}, \033[0;31mTook {}\033[0;0m", modelRegistrationContext.getContextName(), stopwatch1);
        }));
        onModelRegistrationDone();
        ActiveSealModels.MODELS = ImmutableMap.copyOf(ActiveSealModels.getModels());
        WandModels.MODELS = ImmutableMap.copyOf(WandModels.getModels());
        SoulboundMod.LOGGER.info("Finished Registering Models, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    /**
     * This is called after seal registration is done and before the seal map is made unmodifiable. DO NOT CALL. This calls {@link ISoulboundExtension#onSealRegistrationDone() onSealRegistrationDone}
     */
    @ApiStatus.Internal
    public static void onSealRegistrationDone() {
        SoulboundMod.LOGGER.info("Registering DefRegs For Automatically Generated Seal Items");
        Stopwatch stopwatch1 = Stopwatch.createStarted();
        for (DeferredRegister<Item> value : SealRegistrationContext.REGISTRIES.values()) {
            value.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
        SoulboundMod.LOGGER.info("Finished Registering DefRegs For Automaticially Generated Seal Items, \033[0;31mTook {}\033[0;0m", stopwatch1);
        for (ISoulboundExtension extension : EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting After Seal Registration Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onSealRegistrationDone();
            SoulboundMod.LOGGER.info("Starting After Seal Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * This is called after model registration is done and before the model map is made unmodifiable. DO NOT CALL. This calls {@link ISoulboundExtension#onModelRegistrationDone() onModelRegistrationDone}
     */
    @ApiStatus.Internal
    public static void onModelRegistrationDone() {
        for (ISoulboundExtension extension : EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting After Model Registration Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onModelRegistrationDone();
            SoulboundMod.LOGGER.info("Starting After Model Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * Internal method that calls {@link ISoulboundExtension#canRegisterSeal}
     * @param seal the seal to query whether to register
     * @return whether the seal can be registered
     */
    @ApiStatus.Internal
    public static boolean canRegisterSeal(ISeal seal) {
        for (ISoulboundExtension extension : EXTENSIONS) {
            if (!extension.canRegisterSeal(seal)) {
                SoulboundMod.LOGGER.info("Cannot Register Seal {}, Extension {} Forbids It", seal.getId(), extension.getExtensionName());
                return false;
            }
        }
        return true;
    }

    /**
     * Internal method that calls {@link ISoulboundExtension#canRegisterSpellModel}
     * @param model the location of the model to query whether to register
     * @return whether the model can be registered
     */
    @ApiStatus.Internal
    public static boolean canRegisterSpellModel(String name, ResourceLocation model) {
        for (ISoulboundExtension extension : EXTENSIONS) {
            if (!extension.canRegisterSpellModel(name, model)) {
                SoulboundMod.LOGGER.info("Cannot Register Spell Model {}, Extension {} Forbids It", name, extension.getExtensionName());
                return false;
            }
        }
        return true;
    }

    /**
     * Internal method that calls {@link ISoulboundExtension#canRegisterWandModel}
     * @param model the location of the model to query whether to register
     * @return whether the model can be registered
     */
    @ApiStatus.Internal
    public static boolean canRegisterWandModel(String name, ResourceLocation model) {
        for (ISoulboundExtension extension : EXTENSIONS) {
            if (!extension.canRegisterWandModel(name, model)) {
                SoulboundMod.LOGGER.info("Cannot Register Wand Model {}, Extension {} Forbids It", name, extension.getExtensionName());
                return false;
            }
        }
        return true;
    }

    /**
     * called after a seal has been registered
     * calls {@link ISoulboundExtension#onRegisterSeal onRegisterSeal}
     */
    @ApiStatus.Internal
    public static void onRegisterSeal(ISeal seal) {
        for (ISoulboundExtension extension: EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting On Register Tasks For Seal {} in extension {}", seal.getId(), extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onRegisterSeal(seal);
            SoulboundMod.LOGGER.info("Finished On Register Tasks For Seal {} in extension {}, \033[0;31mTook {}\033[0;0m", seal.getId(), extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * called after a model has been registered
     * calls {@link ISoulboundExtension#onRegisterModel  onRegisterModel}
     * */
    @ApiStatus.Internal
    public static void onRegisterModel(ResourceLocation model, boolean isSpellModel) {
        for (ISoulboundExtension extension: EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting On Register Tasks For {} Model {} in extension {}", isSpellModel ? "Spell": "Wand", model, extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onRegisterModel(model, isSpellModel);
            SoulboundMod.LOGGER.info("Finished On Register Tasks For {} Model {} in extension {}, \033[0;31mTook {}\033[0;0m", isSpellModel ? "Spell": "Wand", model, extension.getExtensionName(), stopwatch);
        }
    }


    /**
     * Utilizes {@link #findInstances} in order to find all soulbound extension classes
     * @return The list of soulbound extension classes
     */
    @ApiStatus.Internal
    public static List<ISoulboundExtension> findAllAnnotatedSoulboundExtensions() {
        return findInstances(Extension.class, ISoulboundExtension.class);
    }

    /**
     * Finds all classes with a certain annotation that extend a certain class
     * @param annotationClass the class that something has to be annotated with in order to be added
     * @param instanceClass the class that something has to extend to be added (if you want none, use {@link Object})
     * @return All classes that match these parameters
     */
    @SuppressWarnings("SameParameterValue")
    @ApiStatus.Internal
    private static <T> List<T> findInstances(Class<?> annotationClass, Class<T> instanceClass) {
        Type annotationType = Type.getType(annotationClass);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<String> extensionClassNames = new LinkedHashSet<>();
        SoulboundMod.LOGGER.info("Finding Extension Classes By Annotation");
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (Objects.equals(a.annotationType(), annotationType)) {
                    String memberName = a.memberName();
                    extensionClassNames.add(memberName);
                    String[] splitName = memberName.split("\\.");
                    SoulboundMod.LOGGER.info("Found Extension {}", splitName[splitName.length - 1]);
                }
            }
        }
        SoulboundMod.LOGGER.info("Found Extension Classes By Annotation, \033[0;31mTook {}\033[0;0m", stopwatch2);
        List<T> instances = new ArrayList<>();
        SoulboundMod.LOGGER.info("Adding All Found Extensions");
        Stopwatch stopwatch3 = Stopwatch.createStarted();
        for (String className : extensionClassNames) {
            try {
                String[] splitName = className.split("\\.");
                Stopwatch stopwatch = Stopwatch.createStarted();
                Class<?> asmClass = Class.forName(className);
                Stopwatch stopwatch1 = Stopwatch.createStarted();
                SoulboundMod.LOGGER.info("Constructing Extension {}", splitName[splitName.length - 1]);
                Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
                Constructor<? extends T> constructor = asmInstanceClass.getDeclaredConstructor();
                T instance = constructor.newInstance();
                SoulboundMod.LOGGER.info("Finished Constructing Extension {}, \033[0;31mTook {}\033[0;0m", splitName[splitName.length - 1], stopwatch1);
                instances.add(instance);
                SoulboundMod.LOGGER.info("Found & Added Extension {}, \033[0;31mTook {}\033[0;0m", splitName[splitName.length - 1], stopwatch);
            } catch (ReflectiveOperationException | LinkageError e) {
                SoulboundMod.LOGGER.error("Failed to load: {}", className, e);
            }
        }
        SoulboundMod.LOGGER.info("Finished Adding All Found Extensions, \033[0;31mTook {}\033[0;0m", stopwatch3);
        return instances;
    }

    /**
     * Registers all classes with {@link Extension @Extension} annotation that implement {@link ISoulboundExtension}
     * Also prints some debug log stuff
     */
    @ApiStatus.Internal
    public static void registerAnnotatedExtensions() {
        SoulboundMod.LOGGER.info("Automatically Registering Extensions");
        Stopwatch stopwatch = Stopwatch.createStarted();
        Stopwatch findStopwatch = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Finding Extensions");
        List<ISoulboundExtension> extensions = findAllAnnotatedSoulboundExtensions();
        SoulboundMod.LOGGER.info("Started Sorting Extensions by priority");
        Stopwatch stopwatch1 = Stopwatch.createStarted();
        extensions.sort(Comparator.comparingInt(ISoulboundExtension::priority));
        SoulboundMod.LOGGER.info("Sorted Extensions by priority, \033[0;31mTook {}\033[0;0m", stopwatch1);
        SoulboundMod.LOGGER.info("Found All Extensions, \033[0;31mTook {}\033[0;0m", findStopwatch);
        SoulboundMod.LOGGER.info("Registering Found Extensions");
        Stopwatch registerStopwatch = Stopwatch.createStarted();
        extensions.forEach(extension -> {
            SoulboundMod.LOGGER.info("Registering Extension {}", extension.getExtensionName());
            Stopwatch stopwatch2 = Stopwatch.createStarted();
            registerExtension(extension);
            SoulboundMod.LOGGER.info("Finished Registering Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch2);
            SoulboundMod.LOGGER.info("Executing After Register Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch3 = Stopwatch.createStarted();
            extension.onRegistered();
            SoulboundMod.LOGGER.info("Finished After Register Tasks Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch3);
        });
        SoulboundMod.LOGGER.info("Finished Registering Found Extensions, \033[0;31mTook {}\033[0;0m", registerStopwatch);
        SoulboundMod.LOGGER.info("Finished Automatically Registering Extensions, \033[0;31mTotal Time Taken: {}\033[0;0m", stopwatch);
    }
}
