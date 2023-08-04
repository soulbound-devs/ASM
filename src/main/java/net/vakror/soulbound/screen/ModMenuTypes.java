package net.vakror.soulbound.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.soulbound.SoulboundMod;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SoulboundMod.MOD_ID);

    public static final RegistryObject<MenuType<WandImbuingMenu>> WAND_IMBUING_MENU = registerMenuType(WandImbuingMenu::new, "wand_imbuing_station_menu");

    public static final RegistryObject<MenuType<SoulSolidifierMenu>> SOUL_SOLIDIFIER_MENU = registerMenuType(SoulSolidifierMenu::new, "soul_solidifier_menu");

    public static final RegistryObject<MenuType<SoulExtractorMenu>> SOUL_EXTRACTOR_MENU = registerMenuType(SoulExtractorMenu::new, "soul_extractor_menu");

    public static final RegistryObject<MenuType<SackMenu>> SACK_MENU = registerMenuType(SackMenu::new, "sack_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
