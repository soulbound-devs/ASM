package net.vakror.soulbound.mod.seal.seals.activatable.tool;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.seal.SealProperty;

import java.util.List;

public class ScytheSeal extends OffensiveToolSeal {
    public ScytheSeal() {
        super("scythe", TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(SoulboundMod.MOD_ID, "mineable/scythe")), 3.5f);
    }

    @Override
    public float getDamage() {
        return 4f;
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return Items.DIAMOND_AXE.useOn(context);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("weapon"));
        return super.properties();
    }
}
