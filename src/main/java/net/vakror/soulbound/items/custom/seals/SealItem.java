package net.vakror.soulbound.items.custom.seals;

import net.minecraft.world.item.Item;
import net.vakror.soulbound.seal.seals.MiningSeal;

public class SealItem extends Item {
    private final String id;

    public String getId() {
        return id;
    }

    public SealItem(Properties pProperties, String id) {
        super(pProperties);
        this.id = id;
    }

}
