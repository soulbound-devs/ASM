package net.vakror.asm.util;

import java.util.ArrayList;

public class BetterArrayList<T> extends ArrayList<T> {
    @Override
    public boolean add(T object) {
        if (!this.contains(object)) {
            return super.add(object);
        }
        return false;
    }
}
