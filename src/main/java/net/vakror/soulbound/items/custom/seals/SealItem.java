package net.vakror.soulbound.items.custom.seals;

import net.minecraft.world.item.Item;

public class SealItem extends Item {
    private final String id;
    private final int type;

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public SealItem(Properties pProperties, String id, int type) {
        super(pProperties);
        this.id = id;
        this.type = type;
    }

}
