package net.vakror.soulbound.seal.seals.amplifying.sack;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.seal.type.amplifying.SackAmplifyingSeal;

public class PickupSeal extends SackAmplifyingSeal {
    public PickupSeal() {
        super(new ResourceLocation(SoulboundMod.MOD_ID,"pickup"));
    }
}
