package net.vakror.soulbound.mod.extension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.vakror.soulbound.api.Extension;
import net.vakror.soulbound.api.ISoulboundExtension;
import net.vakror.soulbound.api.context.DungeonRegistrationContext;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.hammerspace.dungeon.level.DungeonLevels;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DefaultDungeonTypes;
import net.vakror.soulbound.mod.items.ModItems;
import net.vakror.soulbound.mod.seal.seals.activatable.SwordSeal;
import net.vakror.soulbound.mod.seal.seals.activatable.tool.AxingSeal;
import net.vakror.soulbound.mod.seal.seals.activatable.tool.HoeingSeal;
import net.vakror.soulbound.mod.seal.seals.activatable.tool.PickaxingSeal;
import net.vakror.soulbound.mod.seal.seals.amplifying.sack.ColumnUpgradeSeal;
import net.vakror.soulbound.mod.seal.seals.amplifying.sack.PickupSeal;
import net.vakror.soulbound.mod.seal.seals.amplifying.sack.RowUpgradeSeal;
import net.vakror.soulbound.mod.seal.seals.amplifying.sack.StackSizeUpgradeSeal;
import net.vakror.soulbound.mod.seal.seals.amplifying.wand.haste.HasteSeal;

import java.util.List;

@Extension
public class DefaultSoulboundExtension implements ISoulboundExtension {
    @Override
    public void registerModels(ModelRegistrationContext context) {
        context.registerSpellModel("pickaxing", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        context.registerSpellModel("hoeing", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/hoeing/base.obj"));
        context.registerSpellModel("shoveling", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));
        context.registerSpellModel("swording", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/swording/base.obj"));
        context.registerSpellModel("axing", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/axing/base.obj"));
        context.registerSpellModel("scythe", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/active_seal/pickaxing/base.obj"));

        context.registerWandModel("ancient_oak", new ResourceLocation(SoulboundMod.MOD_ID, "models/obj/wand/ancient_oak/base.obj"));
    }

    @Override
    public void registerSeals(SealRegistrationContext context) {
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
    public void registerDungeonLevels(DungeonRegistrationContext context) {
        context.registerDungeonLevel(DungeonLevels.LABYRINTH_50, 50, 0);
    }

    @Override
    public void registerDungeonFileLocations(DungeonRegistrationContext context) {
        context.registerDungeonFileLocation(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_0"), 50, 0);
        context.registerDungeonFileLocation(new ResourceLocation(SoulboundMod.MOD_ID, "dungeon_50_1"), 50, 0);
    }

    @Override
    public void registerDungeonTypes(DungeonRegistrationContext context) {
        context.registerDungeonType(DefaultDungeonTypes.DEFAULT);
        context.registerDungeonType(DefaultDungeonTypes.DARK_CREEPY);
        context.registerDungeonType(DefaultDungeonTypes.DEEP_BLOOD);
        context.registerDungeonType(DefaultDungeonTypes.OLD_RUINS);
        context.registerDungeonType(DefaultDungeonTypes.AMETHYST_VOID);
        context.registerDungeonType(DefaultDungeonTypes.GILDED_HOARD);
        context.registerDungeonType(DefaultDungeonTypes.ANCIENT_ENDER);
    }

    @Override
    public ResourceLocation getExtensionName() {
        return new ResourceLocation(SoulboundMod.MOD_ID, "default");
    }
}
