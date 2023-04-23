package net.vakror.asm.seal.type;

import net.minecraft.world.item.Item;
import net.vakror.asm.seal.ISeal;

public abstract class ItemAmplificationSeal extends BaseSeal implements ISeal {

    public ItemAmplificationSeal(String id) {
        super(id, false);
    }

    public abstract void enhanceItem(Item item);
}
