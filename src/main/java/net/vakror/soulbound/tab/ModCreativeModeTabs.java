package net.vakror.soulbound.tab;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.ModItems;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SoulboundMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SOULBOUND_TAB = CREATIVE_MODE_TAB_REGISTER.register("soulbound", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).icon(() -> ModItems.SOUL.get().getDefaultInstance()).build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TAB_REGISTER.register(bus);
    }
}