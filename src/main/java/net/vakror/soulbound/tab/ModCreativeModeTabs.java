package net.vakror.soulbound.tab;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.ModItems;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_REGISTER = DeferredRegister.create(SoulboundMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final DeferredHolder<CreativeModeTab> SOULBOUND_TAB = CREATIVE_MODE_TAB_REGISTER.register("soulbound", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).icon(() -> ModItems.SOUL.get().getDefaultInstance()).build());
}