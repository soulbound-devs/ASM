package net.vakror.soulbound.api.context;

import com.google.common.base.Stopwatch;
import net.minecraft.resources.ResourceLocation;
import net.vakror.registry.jamesregistryapi.api.context.IRegistrationContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.model.models.WandModelReaders;
import net.vakror.soulbound.model.wand.api.IWandModelReader;

public class WandModelReaderRegistrationContext implements IRegistrationContext {
    /**
     * Registers a wand model reader
     *
     * @param name the name of the reader
     * @param reader the reader to register
     */
    public void registerWandModelReader(ResourceLocation name, IWandModelReader reader) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        WandModelReaders.register(name, reader);
        SoulboundMod.LOGGER.info("Registered Wand Model Reader {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
    }

    public void addItemForReader(ResourceLocation reader, ResourceLocation item) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        WandModelReaders.addItem(item, reader);
        SoulboundMod.LOGGER.info("Registered Item {} for Wand Model Reader {}, \033[0;31mTook {}\033[0;0m", item, reader, stopwatch);
    }

    public void removeItemForReader(ResourceLocation reader, ResourceLocation item) {
        if (!WandModelReaders.ITEMS.containsKey(item)) {
            SoulboundMod.LOGGER.error("Attempted To Unregister Item {} From Unpaired Model Reader {}", item, reader);
        } else {
            SoulboundMod.LOGGER.info("Starting Unregistration For Item {} From Wand Model Reader {}", item, reader);
            Stopwatch stopwatch = Stopwatch.createStarted();
            WandModelReaders.ITEMS.remove(item, reader);
            SoulboundMod.LOGGER.info("Finished Unregistration For Item {} From Wand Model Reader {}, \033[0;31mTook {}\033[0;0m", item, reader, stopwatch);
        }
    }

    /**
     * Used to unregister a wand model reader from the registry
     * @param name the name of the wand model reader to unregister
     */
    @Deprecated
    public void unregisterWandModelReader(ResourceLocation name) {
        if (!WandModelReaders.READERS.containsKey(name)) {
            SoulboundMod.LOGGER.error("Attempted To Unregister Non Existent Wand Model Reader {}", name);
        } else {
            SoulboundMod.LOGGER.info("Starting Unregistration For Wand Model Reader {}", name);
            Stopwatch stopwatch = Stopwatch.createStarted();
            WandModelReaders.READERS.remove(name);
            SoulboundMod.LOGGER.info("Finished Unregistration On Wand Model Reader {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
        }
    }

    /**
     * Exchanges one wand model reader with another to override
     * @param name the name of the reader to modify
     * @param newModel the wand model reader to replace the old one with
     */
    @Deprecated
    public void modifyWandModel(ResourceLocation name, IWandModelReader newModel) {
        if (!WandModelReaders.READERS.containsKey(name)) {
            SoulboundMod.LOGGER.error("Attempted To Modify Non Existent Wand Model Reader {}", name);
        } else {
            SoulboundMod.LOGGER.info("Starting Modification On Wand Model Reader {}", name);
            Stopwatch stopwatch = Stopwatch.createStarted();
            WandModelReaders.READERS.replace(name, newModel);
            SoulboundMod.LOGGER.info("Finished Modification On Wand Model Reader {}, \033[0;31mTook {}\033[0;0m", name, stopwatch);
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