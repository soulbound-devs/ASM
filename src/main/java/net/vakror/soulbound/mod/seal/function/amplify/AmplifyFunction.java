package net.vakror.soulbound.mod.seal.function.amplify;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.mod.seal.ISeal;
import net.vakror.soulbound.mod.seal.function.use.UseFunction;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AmplifyFunction {
    protected List<ISeal> affectedSeals;

    public List<ISeal> getAffectedSeals() {
        return affectedSeals;
    }

    public void setAffectedSeals(List<ISeal> affectedSeals) {
        this.affectedSeals = affectedSeals;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return ImmutableMultimap.of();
    }

    public void executeOther(ItemStack stack) {

    }

    @Nullable
    public UseFunction getUseFunction() {
        return null;
    }
}