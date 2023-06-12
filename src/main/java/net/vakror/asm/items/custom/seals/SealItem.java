package net.vakror.asm.items.custom.seals;

import net.minecraft.world.item.Item;
import net.vakror.asm.seal.SealType;

public class SealItem extends Item {
    private final String id;
    private final SealType type;

    public SealType getType() {
        return type;
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
