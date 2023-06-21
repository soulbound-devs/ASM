package net.vakror.asm.seal.type;

import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.AttributeModifiying;
import net.vakror.asm.seal.SealProperty;

import java.util.List;

public abstract class ActivatableSeal extends BaseSeal implements AttributeModifiying {

    public float swingSpeed;

    public ActivatableSeal(String id, float swingSpeed) {
        super(id, true);
        this.swingSpeed = swingSpeed;
    }

    public abstract InteractionResult useAction(UseOnContext context);

    public abstract float getDamage();

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        if (!attributeModifiers.build().containsKey(Attributes.ATTACK_SPEED)) {
            attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(getId() + "_swing_speed", -swingSpeed, AttributeModifier.Operation.ADDITION));
        }
        return AttributeModifiying.super.getAttributeModifiers();
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("activatable"));
        return super.properties();
    }
}
