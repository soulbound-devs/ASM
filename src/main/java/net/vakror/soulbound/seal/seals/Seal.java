package net.vakror.soulbound.seal.seals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.soulbound.seal.ISeal;

public class Seal implements ISeal {
    private final String id;

    public Seal(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
