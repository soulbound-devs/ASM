package net.vakror.soulbound.extension.dungeon.builtin;

import net.vakror.soulbound.compat.dungeon.Dungeon;

public class DefaultDungeon extends Dungeon {
    @Override
    public boolean isEnterable() {
        return true;
    }
}
