package net.vakror.soulbound.api.context;

import com.google.common.base.Stopwatch;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.registry.jamesregistryapi.api.context.IRegistrationContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.seals.SealItem;
import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealRegistry;
import net.vakror.soulbound.seal.SealType;
import net.vakror.soulbound.seal.Tooltip;
import net.vakror.soulbound.seal.tier.seal.Tiered;
import net.vakror.soulbound.seal.tier.sealable.ISealableTier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
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
        SoulboundMod.LOGGER.info("Checking If Seal {} Can Be Registered", seal.getId());
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Seal {} CAN Be Registered, \033[0;31mChecks Took {}\033[0;0m", seal.getId(), stopwatch2);
        SealRegistry.addSeal(seal, seal.getType());
        DeferredRegister<Item> register;
        if (!REGISTRIES.containsKey(modId)) {
            register = DeferredRegister.create(Registries.ITEM, modId);
        } else {
            register = REGISTRIES.get(modId);
        }

        DeferredHolder<Item, SealItem> sealItem = register.register(seal.getId(), () -> new SealItem(
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

    public void registerSealWithCustomItem(@NotNull ISeal seal, @NotNull DeferredHolder<Item, ? extends Item> item) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Checking If Seal {} Can Be Registered", seal.getId());
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Seal {} CAN Be Registered, \033[0;31mChecks Took {}\033[0;0m", seal.getId(), stopwatch2);
        SealRegistry.addSeal(seal, seal.getType());
        SealRegistry.sealItems.put(seal.getId(), item);
        SoulboundMod.LOGGER.info("Registered Seal {} of type {}, \033[0;31mTook {}\033[0;0m", seal.getId(), seal.getType(), stopwatch);
    }

    /**
     * This method is used to register a seal. This method will <B>AUTOMATICIALLY</B> create an item. If you want to use your own item, see {@link #registerSealWithCustomItem}
     *
     * @param modId the mod id that your seal belongs to
     * @param tiered the tiered seal object to register
     * @param properties the item properties of your item
     * @param maxStack a function taking in the wands' tier and outputting the max amount of that seal allowed on wand
     * @param tooltip the tooltip shown in the seal item; can be constructed using {@link Tooltip.TooltipBuilder} and {@link Tooltip.TooltipComponentBuilder}
     */
    public <T extends Tiered> void registerTieredSeal(@NotNull String modId, @NotNull T tiered, @NotNull Item.Properties properties, @Nullable ToIntFunction<ISealableTier> maxStack, @Nullable Tooltip tooltip) {
        if ((tiered instanceof ISeal tieredSeal)) {
            SoulboundMod.LOGGER.info("Registering All Tiers For Seal {} of type {} for mod {}", tieredSeal.getId(), tieredSeal.getType().name().toLowerCase(), modId);
            Stopwatch stopwatch1 = Stopwatch.createStarted();
            for (ISeal seal : tiered.getAllSeals()) {
                SoulboundMod.LOGGER.info("Registering Tier {} For Seal {}", ((Tiered) seal).getTier(), seal.getId());
                Stopwatch stopwatch = Stopwatch.createStarted();
                SoulboundMod.LOGGER.info("Checking If Seal {} Of Tier {} Can Be Registered", seal.getId(), ((Tiered) seal).getTierId());
                Stopwatch stopwatch2 = Stopwatch.createStarted();
                    SoulboundMod.LOGGER.info("Seal {} Of Tier {} CAN Be Registered, \033[0;31mChecks Took {}\033[0;0m", seal.getId(), ((Tiered) seal).getTierId(), stopwatch2);
                    SealRegistry.addSeal(seal, seal.getType());
                    DeferredRegister<Item> register;
                    if (!REGISTRIES.containsKey(modId)) {
                        register = DeferredRegister.create(Registries.ITEM, modId);
                    } else {
                        register = REGISTRIES.get(modId);
                    }

                    DeferredHolder<Item, SealItem> sealItem = register.register(seal.getId(), () -> new SealItem(
                            properties,
                            seal.getId(),
                            seal.getType(),
                            maxStack == null ? (tier) -> 1 : maxStack,
                            tooltip == null ? Tooltip.empty() : tooltip
                    ));
                    REGISTRIES.put(modId, register);

                    SealRegistry.sealItems.put(seal.getId(), sealItem);
                    SoulboundMod.LOGGER.info("Finished Registering Tier {} For Seal {}, \033[0;31mTook {}\033[0;0m", ((Tiered) seal).getTier(), seal.getId(), stopwatch);
            }
            SoulboundMod.LOGGER.info("Finished Registering All Tiers For Seal {} of type {} for mod {}, \033[0;31mTook {}\033[0;0m", tieredSeal.getId(), tieredSeal.getType().name().toLowerCase(), modId, stopwatch1);
        } else {
            SoulboundMod.LOGGER.error("Attempted To Register Tiered Seal That Was Not A Seal, Skipping!");
        }
    }

    /**
     * Similar to {@link #registerSeal} in that it registers a seal, but instead takes in a sealItem
     *
     * @param tiered the tiered object to register. The tier of this should always be zero
     * @param items the item for your seal
     */
    public <T extends Tiered> void registerTieredSealWithCustomItem(@NotNull T tiered, @NotNull List<DeferredHolder<Item, ? extends Item>> items) {
        if ((tiered instanceof ISeal tieredSeal)) {
            SoulboundMod.LOGGER.info("Registering All Tiers For Seal {} of type {}", tieredSeal.getId().split("_tier_0")[0], tieredSeal.getType().name().toLowerCase());
            Stopwatch stopwatch1 = Stopwatch.createStarted();
            for (ISeal seal : tiered.getAllSeals()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                SoulboundMod.LOGGER.info("Registering Tier {} For Seal {}", ((Tiered) seal).getTier(), seal.getId());
                Stopwatch stopwatch2 = Stopwatch.createStarted();
                    SealRegistry.addSeal(seal, seal.getType());
                    SealRegistry.sealItems.put(seal.getId(), items.get(((Tiered) seal).getTier() - 1));
                    SoulboundMod.LOGGER.info("Registered Seal {} of type {}, \033[0;31mTook {}\033[0;0m", seal.getId(), seal.getType(), stopwatch);
            }
            SoulboundMod.LOGGER.info("Finished Registering All Tiers For Seal {} of type {}, \033[0;31mTook {}\033[0;0m", tieredSeal.getId(), tieredSeal.getType().name().toLowerCase(), stopwatch1);
        } else {
            SoulboundMod.LOGGER.error("Attempted To Register Tiered Seal That Was Not A Seal, Skipping!");
        }
    }

    /**
     * Used to unregister a seal from the registry
     * @param name the name of the seal to unregister
     * @param type the type of the seal to unregister
     */
    @Deprecated
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
    @Deprecated
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
    public ResourceLocation getName() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "default");
    }
}
