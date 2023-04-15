package net.vakror.asm.items.custom.seals;

import net.minecraft.world.item.Item;
import net.vakror.asm.seal.SealType;

public class SealItem extends Item {
    private final String id;
    private final SealType type;

    public int getType() {
        return type.getId();
    }

    public String getId() {
        return id;
    }

    public SealItem(Properties pProperties, String id, SealType type) {
        super(pProperties);
        this.id = id;
        this.type = type;
    }

}
