package net.vakror.soulbound.extension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.registry.jamesregistryapi.api.AbstractExtension;
import net.vakror.registry.jamesregistryapi.api.RegistryAPI;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.extension.dungeon.DefaultDungeonExtension;
import net.vakror.soulbound.items.ModItems;
import net.vakror.soulbound.seal.seals.activatable.SwordSeal;
import net.vakror.soulbound.seal.seals.activatable.tool.AxingSeal;
import net.vakror.soulbound.seal.seals.activatable.tool.HoeingSeal;
import net.vakror.soulbound.seal.seals.activatable.tool.PickaxingSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.ColumnUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.PickupSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.RowUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.sack.StackSizeUpgradeSeal;
import net.vakror.soulbound.seal.seals.amplifying.wand.haste.HasteSeal;

import java.util.List;
import java.util.Optional;

public class DefaultSoulboundExtension {
    public static class ModelExtension extends AbstractExtension<ModelRegistrationContext> {

        @Override
        public ResourceLocation getExtensionName() {
            return new ResourceLocation(SoulboundMod.MOD_ID, "default_models");
        }

        @Override
        public void register() {
            context.registerSpellModel("pickaxing", new ResourceLocation(SoulboundMod.MOD_ID, "item/wand/activated/pickaxing"));
            context.registerSpellModel("hoeing", new ResourceLocation(SoulboundMod.MOD_ID, "item/wand/activated/hoeing"));
            context.registerSpellModel("shoveling", new ResourceLocation(SoulboundMod.MOD_ID, "item/wand/activated/shovelling"));
            context.registerSpellModel("swording", new ResourceLocation(SoulboundMod.MOD_ID, "item/wand/activated/swording"));
            context.registerSpellModel("axing", new ResourceLocation(SoulboundMod.MOD_ID, "item/wand/activated/axing"));
            context.registerSpellModel("scythe", new ResourceLocation(SoulboundMod.MOD_ID, "item/wand/activated/scythe"));
        }

        @Override
        public ModelRegistrationContext getDefaultContext() {
            return new ModelRegistrationContext();
        }
    }

    public static class SealExtension extends AbstractExtension<SealRegistrationContext> {
        @Override
        public ResourceLocation getExtensionName() {
            return new ResourceLocation(SoulboundMod.MOD_ID, "default_seals");
        }

        @Override
        public void register() {
            context.registerSealWithCustomItem(new AxingSeal(), ModItems.AXING_SEAL);
            context.registerSealWithCustomItem(new PickaxingSeal(), ModItems.PICKAXING_SEAL);
            context.registerSealWithCustomItem(new HoeingSeal(), ModItems.HOEING_SEAL);
            context.registerSealWithCustomItem(new PickupSeal(), ModItems.SACK_PICKUP_SEAL);
            context.registerSealWithCustomItem(new StackSizeUpgradeSeal(0, 2, AttributeModifier.Operation.MULTIPLY_BASE), ModItems.SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1);
            context.registerSealWithCustomItem(new ColumnUpgradeSeal(0, 2, AttributeModifier.Operation.ADDITION), ModItems.SACK_COLUMN_UPGRADE_SEAL_TIER_1);
            context.registerSealWithCustomItem(new RowUpgradeSeal(0, 2, AttributeModifier.Operation.ADDITION), ModItems.SACK_ROW_UPGRADE_SEAL_TIER_1);
            context.registerSealWithCustomItem(new SwordSeal(), ModItems.SWORDING_SEAL);
            context.registerTieredSealWithCustomItem(new HasteSeal(0), List.of(ModItems.MINING_SPEED_SEAL_TIER_1, ModItems.MINING_SPEED_SEAL_TIER_2, ModItems.MINING_SPEED_SEAL_TIER_3));
        }

        @Override
        public SealRegistrationContext getDefaultContext() {
            return new SealRegistrationContext();
        }

    }

    public static void registerAllExtensions() {
        RegistryAPI.registerExtension(new DefaultDungeonExtension());
        RegistryAPI.registerExtension(new ModelExtension());
        RegistryAPI.registerExtension(new SealExtension());
    }
}
