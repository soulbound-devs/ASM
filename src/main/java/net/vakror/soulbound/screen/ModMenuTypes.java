package net.vakror.soulbound.screen;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.vakror.soulbound.SoulboundMod;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, SoulboundMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<WandImbuingMenu>> WAND_IMBUING_MENU = registerMenuType(WandImbuingMenu::new, "wand_imbuing_station_menu");

    public static final DeferredHolder<MenuType<?>, MenuType<SoulSolidifierMenu>> SOUL_SOLIDIFIER_MENU = registerMenuType(SoulSolidifierMenu::new, "soul_solidifier_menu");

    public static final DeferredHolder<MenuType<?>, MenuType<SoulExtractorMenu>> SOUL_EXTRACTOR_MENU = registerMenuType(SoulExtractorMenu::new, "soul_extractor_menu");

    public static final DeferredHolder<MenuType<?>, MenuType<SackMenu>> SACK_MENU = registerMenuType(SackMenu::new, "sack_menu");

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
