package net.vakror.soulbound.api;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.vakror.soulbound.api.context.DungeonRegistrationContext;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.hammerspace.dungeon.level.DungeonLevel;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DefaultDungeonTypes;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DungeonType;
import net.vakror.soulbound.mod.compat.hammerspace.structure.util.DungeonFileLocations;
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
 * All the methods annotated with {@link ApiStatus.Internal} should never be called
 * The only methods you should call are {@link #registerExtension}, {@link #addRegistrationContext(ModelRegistrationContext)}, and {@link #addRegistrationContext(SealRegistrationContext)}
 * All other calls may be unstable and result in crashes, glitches, or permanent corruption!
 */
public class SoulboundApi {
    /**
     * May be accessed, but never directly updated
     */
    @ApiStatus.Internal
    private static final List<ISoulboundExtension> EXTENSIONS = new ArrayList<>();

    /**
     * May be accessed, but never directly updated
     */
    @ApiStatus.Internal
    private static final List<SealRegistrationContext> SEAL_CONTEXT = new ArrayList<>();

    /**
     * May be accessed, but never directly updated
     */
    @ApiStatus.Internal
    private static final List<ModelRegistrationContext> MODEL_CONTEXT = new ArrayList<>();

    /**
     * May be accessed, but never directly updated
     */
    @ApiStatus.Internal
    private static final List<DungeonRegistrationContext> DUNGEON_CONTEXT = new ArrayList<>();

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
        SoulboundMod.LOGGER.info("Registering Default Dungeon Registration Context");
        Stopwatch stopwatch3 = Stopwatch.createStarted();
        addRegistrationContext(new DungeonRegistrationContext());
        SoulboundMod.LOGGER.info("Finished Registering Default Dungeon Registration Context, \033[0;31mTook {}\033[0;0m", stopwatch3);
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
     * used to add a new dungeon context for whatever advanced things you need to do
     * <br>
     * This must be called before {@link net.minecraftforge.registries.RegisterEvent registration} has fired as that is when seals are registered and!
     * @param context the context to add
     */
    protected static <T extends DungeonRegistrationContext> void addRegistrationContext(T context) {
        DUNGEON_CONTEXT.add(context);
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

        SoulboundMod.LOGGER.info("Finished Registering Models, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    /**
     * <b><h2><i>INTERNAL USE ONLY! DO NOT CALL</i></h2></b>
     */
    @ApiStatus.Internal
    public static void onModify() {
        SoulboundMod.LOGGER.info("Starting On Modify Actions");
        Stopwatch stopwatch = Stopwatch.createStarted();
            EXTENSIONS.forEach((soulboundExtension -> {
                SoulboundMod.LOGGER.info("Starting On Modify Actions using Extension {}", soulboundExtension.getExtensionName());
                Stopwatch stopwatch2 = Stopwatch.createStarted();
                soulboundExtension.onModify();
                SoulboundMod.LOGGER.info("Finished On Modify Actions using Extension {}, \033[0;31mTook {}\033[0;0m", soulboundExtension.getExtensionName(), stopwatch2);
        }));
        SoulboundMod.LOGGER.info("Finished On Modify Actions, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    /**
     * Makes all registries immutable
     */
    @ApiStatus.Internal
    public static void makeRegistriesImmutable() {
        SoulboundMod.LOGGER.info("Making Seal Registries Immutable");
        Stopwatch stopwatch = Stopwatch.createStarted();
        SealRegistry.doneRegistering();
        SoulboundMod.LOGGER.info("Finished Making Seal Registries Immutable, \033[0;31mTook {}\033[0;0m", stopwatch);

        SoulboundMod.LOGGER.info("Making Spell Models Registry Immutable");
        Stopwatch stopwatch1 = Stopwatch.createStarted();
        ActiveSealModels.MODELS = ImmutableMap.copyOf(ActiveSealModels.getModels());
        SoulboundMod.LOGGER.info("Finished Making Spell Models Registry Immutable, \033[0;31mTook {}\033[0;0m", stopwatch1);

        SoulboundMod.LOGGER.info("Making Wand Models Registry Immutable");
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        WandModels.MODELS = ImmutableMap.copyOf(WandModels.getModels());
        SoulboundMod.LOGGER.info("Finished Making Wand Models Registry Immutable, \033[0;31mTook {}\033[0;0m", stopwatch2);

        SoulboundMod.LOGGER.info("Making Dungeon Levels Registry Immutable");
        Stopwatch stopwatch3 = Stopwatch.createStarted();
        DungeonLevel.ALL_LEVELS = ImmutableMultimap.copyOf(DungeonLevel.ALL_LEVELS);
        SoulboundMod.LOGGER.info("Finished Making Dungeon Levels Registry Immutable, \033[0;31mTook {}\033[0;0m", stopwatch3);

        SoulboundMod.LOGGER.info("Making Dungeon File Locations Registry Immutable");
        Stopwatch stopwatch4 = Stopwatch.createStarted();
        DungeonFileLocations.FILES = ImmutableMultimap.copyOf(DungeonFileLocations.FILES);
        SoulboundMod.LOGGER.info("Finished Making Dungeon File Locations Registry Immutable, \033[0;31mTook {}\033[0;0m", stopwatch4);

        SoulboundMod.LOGGER.info("Making Dungeon Types Registry Immutable");
        Stopwatch stopwatch5 = Stopwatch.createStarted();
        DefaultDungeonTypes.ALL_DUNGEON_TYPES = ImmutableList.copyOf(DefaultDungeonTypes.ALL_DUNGEON_TYPES);
        SoulboundMod.LOGGER.info("Finished Making Dungeon Types Registry Immutable, \033[0;31mTook {}\033[0;0m", stopwatch5);
    }

    /**
     * <b><h2><i>INTERNAL USE ONLY! DO NOT CALL</i></h2></b>
     */
    @ApiStatus.Internal
    public static void onDungeonRegister() {
        SoulboundMod.LOGGER.info("Registering Dungeons");
        Stopwatch stopwatch = Stopwatch.createStarted();
        DUNGEON_CONTEXT.forEach((dungeonRegistrationContext -> {
            SoulboundMod.LOGGER.info("Starting Dungeon Registration Using Context {}", dungeonRegistrationContext.getContextName());
            Stopwatch stopwatch1 = Stopwatch.createStarted();
            EXTENSIONS.forEach((soulboundExtension -> {
                onDungeonLevelRegister(soulboundExtension, dungeonRegistrationContext);
                onDungeonFileLocationRegister(soulboundExtension, dungeonRegistrationContext);
                onDungeonTypeRegister(soulboundExtension, dungeonRegistrationContext);
            }));
            SoulboundMod.LOGGER.info("Finished Dungeon Registration using Context {}, \033[0;31mTook {}\033[0;0m", dungeonRegistrationContext.getContextName(), stopwatch1);
        }));
        onDungeonLevelRegistrationDone();
        onDungeonFileLocationRegistrationDone();
        onDungeonTypeRegistrationDone();
        SoulboundMod.LOGGER.info("Finished Registering Dungeons, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    private static void onDungeonLevelRegister(ISoulboundExtension soulboundExtension, DungeonRegistrationContext dungeonRegistrationContext) {
        SoulboundMod.LOGGER.info("Starting Registration Of Dungeon Levels using Extension {} and Context {}", soulboundExtension.getExtensionName(), dungeonRegistrationContext.getContextName());
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        if (Arrays.stream(soulboundExtension.getSealContextTypes().split(", ")).anyMatch((s) -> s.equals(dungeonRegistrationContext.getContextName()))) {
            soulboundExtension.registerDungeonLevels(dungeonRegistrationContext);
        }
        SoulboundMod.LOGGER.info("Finished Registration Of Dungeon Levels using Extension {} and Context {}, \033[0;31mTook {}\033[0;0m", soulboundExtension.getExtensionName(), dungeonRegistrationContext.getContextName(), stopwatch2);
    }

    private static void onDungeonFileLocationRegister(ISoulboundExtension soulboundExtension, DungeonRegistrationContext dungeonRegistrationContext) {
        SoulboundMod.LOGGER.info("Starting Registration Of Dungeon File Locations using Extension {} and Context {}", soulboundExtension.getExtensionName(), dungeonRegistrationContext.getContextName());
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        if (Arrays.stream(soulboundExtension.getSealContextTypes().split(", ")).anyMatch((s) -> s.equals(dungeonRegistrationContext.getContextName()))) {
            soulboundExtension.registerDungeonFileLocations(dungeonRegistrationContext);
        }
        SoulboundMod.LOGGER.info("Finished Registration Of Dungeon File Locations using Extension {} and Context {}, \033[0;31mTook {}\033[0;0m", soulboundExtension.getExtensionName(), dungeonRegistrationContext.getContextName(), stopwatch2);
    }

    private static void onDungeonTypeRegister(ISoulboundExtension soulboundExtension, DungeonRegistrationContext dungeonRegistrationContext) {
        SoulboundMod.LOGGER.info("Starting Registration Of Dungeon Types using Extension {} and Context {}", soulboundExtension.getExtensionName(), dungeonRegistrationContext.getContextName());
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        if (Arrays.stream(soulboundExtension.getSealContextTypes().split(", ")).anyMatch((s) -> s.equals(dungeonRegistrationContext.getContextName()))) {
            soulboundExtension.registerDungeonTypes(dungeonRegistrationContext);
        }
        SoulboundMod.LOGGER.info("Finished Registration Of Dungeon Types using Extension {} and Context {}, \033[0;31mTook {}\033[0;0m", soulboundExtension.getExtensionName(), dungeonRegistrationContext.getContextName(), stopwatch2);
    }

    /**
     * This is called after seal registration is done and before the seal map is made unmodifiable.
     * DO NOT CALL.
     * This calls {@link ISoulboundExtension#onSealRegistrationDone() onSealRegistrationDone}
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
            SoulboundMod.LOGGER.info("Finished After Seal Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * This is called after model registration is done and before the model map is made unmodifiable.
     * DO NOT CALL.
     * This calls {@link ISoulboundExtension#onModelRegistrationDone() onModelRegistrationDone}
     */
    @ApiStatus.Internal
    public static void onModelRegistrationDone() {
        for (ISoulboundExtension extension : EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting After Model Registration Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onModelRegistrationDone();
            SoulboundMod.LOGGER.info("Finished After Model Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * This is called after dungeon level registration is done and before the dungeon level map is made unmodifiable.
     * DO NOT CALL.
     * This calls {@link ISoulboundExtension#onDungeonLevelRegistrationDone() onDungeonLevelRegistrationDone}
     */
    public static void onDungeonLevelRegistrationDone() {
        for (ISoulboundExtension extension : EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting After Dungeon Level Registration Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onDungeonLevelRegistrationDone();
            SoulboundMod.LOGGER.info("Finished After Dungeon Level Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * This is called after dungeon file location registration is done and before the dungeon file location map is made unmodifiable.
     * DO NOT CALL.
     * This calls {@link ISoulboundExtension#onDungeonFileLocationRegistrationDone() onDungeonFileLocationRegistrationDone}
     */
    public static void onDungeonFileLocationRegistrationDone() {
        for (ISoulboundExtension extension : EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting After Dungeon File Location Registration Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onDungeonFileLocationRegistrationDone();
            SoulboundMod.LOGGER.info("Finished After Dungeon File Location Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
        }
    }


    /**
     * This is called after dungeon type registration is done and before the dungeon type map is made unmodifiable.
     * DO NOT CALL.
     * This calls {@link ISoulboundExtension#onDungeonTypeRegistrationDone() onDungeonTypeRegistrationDone}
     */
    public static void onDungeonTypeRegistrationDone() {
        for (ISoulboundExtension extension : EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting After Dungeon Type Registration Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onDungeonTypeRegistrationDone();
            SoulboundMod.LOGGER.info("Finished After Dungeon Type Registration Tasks For Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch);
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
     * Internal method that calls {@link ISoulboundExtension#canRegisterDungeonType}
     * @param type the type that is being registered
     * @return whether the dungeon type will be registered
     */
    @ApiStatus.Internal
    public static boolean canRegisterDungeonType(DungeonType type) {
        for (ISoulboundExtension extension : EXTENSIONS) {
            if (!extension.canRegisterDungeonType(type)) {
                SoulboundMod.LOGGER.info("Cannot Register Dungeon Type {}, Extension {} Forbids It", type.id(), extension.getExtensionName());
                return false;
            }
        }
        return true;
    }

    /**
     * Internal method that calls {@link ISoulboundExtension#canRegisterDungeonLevel}
     * @param dungeonLevel the level that is being registered
     * @param size the size of the level
     * @param level the level of the dungeon level
     * @return whether the dungeon level will be registered
     */
    @ApiStatus.Internal
    public static boolean canRegisterDungeonLevel(DungeonLevel dungeonLevel, int size, int level) {
        for (ISoulboundExtension extension : EXTENSIONS) {
            if (!extension.canRegisterDungeonLevel(dungeonLevel, size, level)) {
                SoulboundMod.LOGGER.info("Cannot Register Dungeon Level {} Of Size {} And Level {}, Extension {} Forbids It", dungeonLevel.getName(), size, level, extension.getExtensionName());
                return false;
            }
        }
        return true;
    }

    /**
     * Internal method that calls {@link ISoulboundExtension#canRegisterDungeonFileLocation}
     * @param size size of this location
     * @param level the level of this location
     * @param location the location of the file to register
     * @return whether the dungeon file location will be registered
     */
    @ApiStatus.Internal
    public static boolean canRegisterDungeonFileLocation(int size, int level, ResourceLocation location) {
        for (ISoulboundExtension extension : EXTENSIONS) {
            if (!extension.canRegisterDungeonFileLocation(size, level, location)) {
                SoulboundMod.LOGGER.info("Cannot Register Dungeon File Location {} Of Size {} And Level {}, Extension {} Forbids It", location, size, level, extension.getExtensionName());
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
     * called after a dungeon level has been registered
     * calls {@link ISoulboundExtension#onRegisterDungeonLevel onRegisterDungeonLevel}
     */
    public static void onRegisterDungeonLevel(DungeonLevel dungeonLevel, int size, int level) {
        for (ISoulboundExtension extension: EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting On Register Tasks For Dungeon Level {} (Size {}, Level {}) in extension {}", dungeonLevel.getName(), size, level, extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onRegisterDungeonLevel(dungeonLevel, size, level);
            SoulboundMod.LOGGER.info("Finished On Register Tasks For Dungeon Level {} (Size {}, Level {}) in extension {}, \033[0;31mTook {}\033[0;0m", dungeonLevel.getName(), size, level, extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * called after a dungeon file location has been registered
     * calls {@link ISoulboundExtension#onRegisterDungeonFileLocation onRegisterDungeonFileLocation}
     */
    public static void onRegisterDungeonFileLocation(int size, int level, ResourceLocation location) {
        for (ISoulboundExtension extension: EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting On Register Tasks For Dungeon File Location {} (Size {}, Level {}) in extension {}", location, size, level, extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onRegisterDungeonFileLocation(size, level, location);
            SoulboundMod.LOGGER.info("Finished On Register Tasks For Dungeon File Location {} (Size {}, Level {}) in extension {}, \033[0;31mTook {}\033[0;0m", location, size, level, extension.getExtensionName(), stopwatch);
        }
    }

    /**
     * called after a dungeon type has been registered
     * calls {@link ISoulboundExtension#onRegisterDungeonType onRegisterDungeonType}
     */
    public static void onRegisterDungeonType(DungeonType type) {
        for (ISoulboundExtension extension: EXTENSIONS) {
            SoulboundMod.LOGGER.info("Starting On Register Tasks For Dungeon Type {} in extension {}", type.id(), extension.getExtensionName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            extension.onRegisterDungeonType(type);
            SoulboundMod.LOGGER.info("Starting On Register Tasks For Dungeon Type {} in extension {}, \033[0;31mTook {}\033[0;0m", type.id(), extension.getExtensionName(), stopwatch);
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
            SoulboundMod.LOGGER.info("Executing On Register Tasks For Extension {}", extension.getExtensionName());
            Stopwatch stopwatch3 = Stopwatch.createStarted();
            extension.onRegistered();
            SoulboundMod.LOGGER.info("Finished On Register Tasks Extension {}, \033[0;31mTook {}\033[0;0m", extension.getExtensionName(), stopwatch3);
        });
        SoulboundMod.LOGGER.info("Finished Registering Found Extensions, \033[0;31mTook {}\033[0;0m", registerStopwatch);
        SoulboundMod.LOGGER.info("Finished Automatically Registering Extensions, \033[0;31mTotal Time Taken: {}\033[0;0m", stopwatch);
    }
}
