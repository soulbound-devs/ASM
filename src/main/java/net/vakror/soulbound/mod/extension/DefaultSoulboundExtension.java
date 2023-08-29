package net.vakror.soulbound.mod.extension;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.api.SoulboundExtension;
import net.vakror.soulbound.api.context.ModelRegistrationContext;
import net.vakror.soulbound.api.context.SealRegistrationContext;
import net.vakror.soulbound.mod.SoulboundMod;
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
import net.vakror.soulbound.mod.util.ArithmeticActionType;

public class DefaultSoulboundExtension extends SoulboundExtension {
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
        context.registerSealWithCustomItem(new HasteSeal.HasteSealTierOne(), ModItems.MINING_SPEED_SEAL_TIER_1);
        context.registerSealWithCustomItem(new HasteSeal.HasteSealTierTwo(), ModItems.MINING_SPEED_SEAL_TIER_2);
        context.registerSealWithCustomItem(new HasteSeal.HasteSealTierThree(), ModItems.MINING_SPEED_SEAL_TIER_3);
        context.registerSealWithCustomItem(new PickupSeal(), ModItems.SACK_PICKUP_SEAL);
        context.registerSealWithCustomItem(new StackSizeUpgradeSeal(1, 2, ArithmeticActionType.MULTIPLY), ModItems.SACK_STACK_SIZE_UPGRADE_SEAL_TIER_1);
        context.registerSealWithCustomItem(new ColumnUpgradeSeal(1, 2, ArithmeticActionType.ADD), ModItems.SACK_COLUMN_UPGRADE_SEAL_TIER_1);
        context.registerSealWithCustomItem(new RowUpgradeSeal(1, 2, ArithmeticActionType.ADD), ModItems.SACK_ROW_UPGRADE_SEAL_TIER_1);
        context.registerSealWithCustomItem(new SwordSeal(), ModItems.SWORDING_SEAL);
    }
}
