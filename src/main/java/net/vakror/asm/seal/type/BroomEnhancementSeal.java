package net.vakror.asm.seal.type;

import net.vakror.asm.entity.BroomEntity;
import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.type.BaseSeal;

public abstract class BroomEnhancementSeal extends BaseSeal implements ISeal {

    public BroomEnhancementSeal(String id) {
        super(id, false);
    }

    public abstract void modifyBroom(BroomEntity entity);
}
