package net.vakror.soulbound.seal.seals.activatable.tool;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.SoulboundMod;

public class ScytheSeal extends OffensiveToolSeal {
    public ScytheSeal() {
        super("scythe", TagKey.create(Registries.BLOCK, new ResourceLocation(SoulboundMod.MOD_ID, "mineable/scythe")), 3.5f);
    }

    @Override
    public float getDamage() {
        return 4f;
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return Items.DIAMOND_AXE.useOn(context);
    }
}
