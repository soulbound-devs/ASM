package net.vakror.asm.seal.type;

import net.vakror.asm.entity.BroomEntity;

public abstract class BroomEnhancementSeal extends BaseSeal {

    public BroomEnhancementSeal(String id) {
        super(id, false);
    }

    public abstract void modifyBroom(BroomEntity entity);
}
