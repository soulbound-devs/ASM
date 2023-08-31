package net.vakror.soulbound.api.context;

import com.google.common.base.Stopwatch;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.api.SoulboundApi;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.items.custom.seals.SealItem;
import net.vakror.soulbound.mod.seal.ISeal;
import net.vakror.soulbound.mod.seal.SealRegistry;
import net.vakror.soulbound.mod.seal.SealType;
import net.vakror.soulbound.mod.seal.Tooltip;
import net.vakror.soulbound.mod.seal.tier.sealable.ISealableTier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public class SealRegistrationContext implements IRegistrationContext {
    /**
     * Stores deferred registers by mod id so that we don't have to reuse them all the time in {@link #registerSeal}
     */
    public static final Map<String, DeferredRegister<Item>> REGISTRIES = new LinkedHashMap<>();

    /**
     * This method is used to register a seal. This method will <B>AUTOMATICIALLY</B> create an item. If you want to use your own item, see {@link #registerSealWithCustomItem}
     *
     * @param modId the mod id that your seal belongs to
     * @param seal the seal object to register
     * @param properties the item properties of your item
     * @param maxStack a function taking in the wands' tier and outputting the max amount of that seal allowed on wand
     * @param tooltip the tooltip shown in the seal item; can be constructed using {@link Tooltip.TooltipBuilder} and {@link Tooltip.TooltipComponentBuilder}
     */
    public void registerSeal(@NotNull String modId, @NotNull ISeal seal, @NotNull Item.Properties properties, @Nullable ToIntFunction<ISealableTier> maxStack, @Nullable Tooltip tooltip) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterSeal(seal)) {
            SealRegistry.addSeal(seal, seal.getType());
            DeferredRegister<Item> register;
            if (!REGISTRIES.containsKey(modId)) {
                register = DeferredRegister.create(Registry.ITEM_REGISTRY, modId);
                ;
            } else {
                register = REGISTRIES.get(modId);
            }

            RegistryObject<Item> sealItem = register.register(seal.getId(), () -> new SealItem(
                    properties,
                    seal.getId(),
                    seal.getType(),
                    maxStack == null ? (tier) -> 1 : maxStack,
                    tooltip == null ? Tooltip.empty() : tooltip
            ));
            REGISTRIES.put(modId, register);

            SealRegistry.sealItems.put(seal.getId(), sealItem);
            SoulboundMod.LOGGER.info("Registered Seal {} of type {} for mod {}, \033[0;31mTook {}\033[0;0m", seal.getId(), seal.getType().name().toLowerCase(), modId, stopwatch);
        }
    }

    /**
     * Similar to {@link #registerSeal} in that it registers a seal, but instead takes in a sealItem
     *
     * @param seal the seal object to register
     * @param item the item for your seal
     */
    public void registerSealWithCustomItem(@NotNull ISeal seal, @NotNull RegistryObject<Item> item) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterSeal(seal)) {
            SealRegistry.addSeal(seal, seal.getType());
            SealRegistry.sealItems.put(seal.getId(), item);
            SoulboundMod.LOGGER.info("Registered Seal {} of type {}, \033[0;31mTook {}\033[0;0m", seal.getId(), seal.getType(), stopwatch);
            SoulboundApi.onRegisterSeal(seal);
        }
    }

    /**
     * Used to unregister a seal from the registry
     * @param name the name of the seal to unregister
     * @param type the type of the seal to unregister
     */
    public void unregisterSeal(String name, SealType type) {
        if (!SealRegistry.allSeals.containsKey(name)) {
            throw new IllegalStateException("Attempted To Unregister Non Existent Seal " + name);
        } else {
            SoulboundMod.LOGGER.info("Starting Unregistration For Seal {}, Of Type {}", name, type.name().toLowerCase());
            Stopwatch stopwatch = Stopwatch.createStarted();
            SealRegistry.allSeals.remove(name);
            switch (type) {
                case PASSIVE -> SealRegistry.passiveSeals.remove(name);
                case OFFENSIVE -> SealRegistry.attackSeals.remove(name);
                case AMPLIFYING -> SealRegistry.amplifyingSeals.remove(name);
            }
            SoulboundMod.LOGGER.info("Finished Unregistration On Seal {}, Of Type {}, \033[0;31mTook {}\033[0;0m", name, type.name().toLowerCase(), stopwatch);
        }
    }

    /**
     * Exchanges one seal with another to override
     * @param name the name of the seal to modify
     * @param type the type of the seal that will be modified
     * @param newSeal the seal to replace the old one with
     */
    public void modifySeal(String name, SealType type, ISeal newSeal) {
        if (!SealRegistry.allSeals.containsKey(name)) {
            throw new IllegalArgumentException("Attempted To Modify Non Existent Seal " + name);
        } else {
            SoulboundMod.LOGGER.info("Starting Modification On Seal {}, Of Type {}", name, type.name().toLowerCase());
            Stopwatch stopwatch = Stopwatch.createStarted();
            SealRegistry.allSeals.replace(name, newSeal);
            switch (type) {
                case PASSIVE -> SealRegistry.passiveSeals.replace(name, newSeal);
                case OFFENSIVE -> SealRegistry.attackSeals.replace(name, newSeal);
                case AMPLIFYING -> SealRegistry.amplifyingSeals.replace(name, newSeal);
            }
            SoulboundMod.LOGGER.info("Finished Modification On Seal {}, Of Type {}, \033[0;31mTook {}\033[0;0m", name, type.name().toLowerCase(), stopwatch);
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
