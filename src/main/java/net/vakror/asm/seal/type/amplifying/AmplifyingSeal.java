package net.vakror.asm.seal.type.amplifying;

import net.minecraft.world.item.Item;
import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.BaseSeal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AmplifyingSeal extends BaseSeal {
    public List<ISeal> requirements = new ArrayList<>();
    public int priority = 0;
    public boolean canStack = false;
    public int maxStackCount = 1;
    Map<String, Object> variables = new HashMap<>();

    public AmplifyingSeal(String id) {
        super(id, false);
    }

    public abstract void amplify(Item item);

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("amplifying"));
        return super.properties();
    }
}
