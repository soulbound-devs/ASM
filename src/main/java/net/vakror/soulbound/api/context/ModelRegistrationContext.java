package net.vakror.soulbound.api.context;

import com.google.common.base.Stopwatch;
import net.minecraft.resources.ResourceLocation;
import net.vakror.registry.jamesregistryapi.api.context.IRegistrationContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.model.models.ActiveSealModels;
import net.vakror.soulbound.model.models.WandModels;

public class ModelRegistrationContext implements IRegistrationContext {
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
    public ResourceLocation getName() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "default");
    }
}