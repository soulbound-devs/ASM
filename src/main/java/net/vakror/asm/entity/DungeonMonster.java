package net.vakror.asm.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class DungeonMonster extends Monster {
    protected DungeonMonster(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
}
