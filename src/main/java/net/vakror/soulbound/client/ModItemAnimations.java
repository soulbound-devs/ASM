package net.vakror.soulbound.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.vakror.soulbound.items.custom.SealableItem;
import org.jetbrains.annotations.Nullable;

public class ModItemAnimations {

    private static final HumanoidModel.ArmPose WAND_POSE = HumanoidModel.ArmPose.create("pose", true, (model, entity, arm) -> {
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.xRot = Mth.clamp(model.head.xRot - 1.9198622F - (model.crouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
            model.rightArm.yRot = model.head.yRot - 0.2617994F;
        } else {
            model.leftArm.xRot = Mth.clamp(model.head.xRot - 1.9198622F - (model.crouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
            model.leftArm.yRot = model.head.yRot + 0.2617994F;
        }
    });
    public static IClientItemExtensions wandAnimation = new IClientItemExtensions() {
        @Override
        public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (itemStack.getItem() instanceof SealableItem) {
                return WAND_POSE;
            }
            return HumanoidModel.ArmPose.ITEM;
        }
    };
}
