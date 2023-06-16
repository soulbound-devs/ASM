package net.vakror.asm.seal.seals.activatable.tool;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.ASMMod;
import net.vakror.asm.seal.SealProperty;

import java.util.List;

public class ScytheSeal extends OffensiveToolSeal {
    public ScytheSeal() {
        super("scythe", new TagKey<>(Registries.BLOCK, new ResourceLocation(ASMMod.MOD_ID, "mineable/scythe")), 5);
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
