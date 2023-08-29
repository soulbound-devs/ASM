package net.vakror.soulbound.api.context;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.mod.items.custom.seals.SealItem;
import net.vakror.soulbound.mod.seal.ISeal;
import net.vakror.soulbound.mod.seal.SealRegistry;
import net.vakror.soulbound.mod.seal.Tooltip;
import net.vakror.soulbound.mod.seal.tier.sealable.ISealableTier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class SealRegistrationContext implements IRegistrationContext {
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
        SealRegistry.addSeal(seal, seal.getType());
        RegistryObject<Item> sealItem = DeferredRegister.create(Registry.ITEM_REGISTRY, modId).register(seal.getId(), () -> new SealItem(
                properties,
                seal.getId(),
                seal.getType(),
                maxStack == null ? (tier) -> 1: maxStack,
                tooltip == null ? Tooltip.empty(): tooltip
        ));

        SealRegistry.sealItems.put(seal.getId(), sealItem);
    }

    /**
     * Similar to {@link #registerSeal} in that it registers a seal, but instead takes in a sealItem
     *
     * @param seal the seal object to register
     * @param item the item for your seal
     */
    public void registerSealWithCustomItem(@NotNull ISeal seal, @NotNull RegistryObject<Item> item) {
        SealRegistry.addSeal(seal, seal.getType());
        SealRegistry.sealItems.put(seal.getId(), item);
    }

    /**
     * @return the name of all default soulbound contexts will always be "default"
     */
    @Override
    public String getName() {
        return "default";
    }
}
