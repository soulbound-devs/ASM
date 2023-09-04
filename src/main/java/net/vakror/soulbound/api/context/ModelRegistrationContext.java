package net.vakror.soulbound.api.context;

import com.google.common.base.Stopwatch;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.api.SoulboundApi;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.model.models.ActiveSealModels;
import net.vakror.soulbound.mod.model.models.WandModels;

/**
 * The Context Which Is Passed Into {@link net.vakror.soulbound.api.ISoulboundExtension#registerModels registerModels}
 * Used to register/unregister/modify wand/spell models
 */
public class ModelRegistrationContext implements IRegistrationContext {
    /**
     * Registers a CUSTOM model for a wand. Base models are registered in your model JSON
     *
     * @param name the name of your wand model
     * @param location the location for the OBJ file for your wand, must include the .obj file extension. Example: "soulbound:models/obj/wand/ancient_oak/base.obj"
     */
    public void registerWandModel(String name, ResourceLocation location) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterWandModel(name, location)) {
            WandModels.registerModel(name, location);
            SoulboundMod.LOGGER.info("Registered Wand Model {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * Registers a model for an active seal (spell)
     *
     * @param name the name of your active seal model (if for a seal and not custom model, must match seal registry name <b>EXACTLY</b>
     * @param location the location for the OBJ file for your seal, must include the .obj file extension and have another file named the same, with "_outline" at the end in the same directory. Example: "soulbound:models/obj/seal/pickaxing/base.obj"
     */
    public void registerSpellModel(String name, ResourceLocation location) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterSpellModel(name, location)) {
            ActiveSealModels.registerModel(name, location);
            SoulboundMod.LOGGER.info("Registered Spell Model {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * Used to unregister a wand model from the registry
     * @param name the name of the wand model to unregister
     */
    @Deprecated
    public void unregisterWandModel(String name) {
        if (!WandModels.MODELS.containsKey(name)) {
            throw new IllegalStateException("Attempted To Unregister Non Existent Wand Model " + name);
        } else {
            SoulboundMod.LOGGER.info("Starting Unregistration For Wand Model {}", name);
            Stopwatch stopwatch = Stopwatch.createStarted();
            WandModels.MODELS.remove(name);
            SoulboundMod.LOGGER.info("Finished Unregistration On Wand Model {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * Used to unregister a spell model from the registry
     * @param name the name of the spell model to unregister
     */
    @Deprecated
    public void unregisterSpellModel(String name) {
        if (!ActiveSealModels.MODELS.containsKey(name)) {
            throw new IllegalStateException("Attempted To Unregister Non Existent Spell Model " + name);
        } else {
            SoulboundMod.LOGGER.info("Starting Unregistration For Spell Model {}", name);
            Stopwatch stopwatch = Stopwatch.createStarted();
            ActiveSealModels.MODELS.remove(name);
            SoulboundMod.LOGGER.info("Finished Unregistration On Spell Model {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * Exchanges one spell model with another to override
     * @param name the name of the model to modify
     * @param newModel the spell model to replace the old one with
     */
    @Deprecated
    public void modifySpellModel(String name, ResourceLocation newModel) {
        if (!ActiveSealModels.MODELS.containsKey(name)) {
            throw new IllegalArgumentException("Attempted To Modify Non Existent Spell Model " + name);
        } else {
            SoulboundMod.LOGGER.info("Starting Modification On Spell Model {}", name);
            Stopwatch stopwatch = Stopwatch.createStarted();
            ActiveSealModels.MODELS.replace(name, newModel);
            SoulboundMod.LOGGER.info("Finished Modification On Spell Model {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * Exchanges one wand model with another to override
     * @param name the name of the model to modify
     * @param newModel the wand model to replace the old one with
     */
    @Deprecated
    public void modifyWandModel(String name, ResourceLocation newModel) {
        if (!WandModels.MODELS.containsKey(name)) {
            throw new IllegalArgumentException("Attempted To Modify Non Existent Wand Model " + name);
        } else {
            SoulboundMod.LOGGER.info("Starting Modification On Wand Model {}", name);
            Stopwatch stopwatch = Stopwatch.createStarted();
            WandModels.MODELS.replace(name, newModel);
            SoulboundMod.LOGGER.info("Finished Modification On Wand Model {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * @return the name of all default soulbound contexts will always be "default"
     */
    @Override
    public String getContextName() {
        return "default";
    }
}
