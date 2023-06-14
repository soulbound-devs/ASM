package net.vakror.asm.items.custom.seals;

import net.minecraft.world.item.Item;
import net.vakror.asm.seal.SealType;

public class SealItem extends Item {
    private final String id;
    private final SealType type;
    private final boolean canAddMultiple;
    private final int maxSealStack;

    public SealType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public SealItem(Properties pProperties, String id, SealType type) {
        this(pProperties, id, type, false, 1);
    }

    public SealItem(Properties pProperties, String id, SealType type, boolean canAddMultiple, int maxSealStack) {
        super(pProperties);
        this.id = id;
        this.type = type;
        this.canAddMultiple = canAddMultiple;
        this.maxSealStack = maxSealStack;
    }

    public boolean canAddMultiple() {
        return canAddMultiple;
    }

    public int getMaxSealStack() {
        return maxSealStack;
    }
}
