package net.vakror.soulbound.mod.compat.jei.recipe.solidifying;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.mod.soul.SoulType;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A recipe in JEI that displays soul solidifying stuff.
 */
public interface ISoulSolidifyingRecipe {
    /**
     * The soul type.
     */
    @Unmodifiable
    SoulType getSoulType();

    /**
     * The ingot
     */
    @Unmodifiable
    Item getIngotItem();

    /**
     * The output soul
     */
    @Unmodifiable
    ItemStack getOutput();
}
