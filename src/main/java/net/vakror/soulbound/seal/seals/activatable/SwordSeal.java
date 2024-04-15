package net.vakror.soulbound.seal.seals.activatable;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.seal.type.AttackSeal;

public class SwordSeal extends AttackSeal {
    public SwordSeal() {
        super(new ResourceLocation(SoulboundMod.MOD_ID, "swording"), 3f);
    }

    @Override
    public float getDamage() {
        return 7f;
    }
}
