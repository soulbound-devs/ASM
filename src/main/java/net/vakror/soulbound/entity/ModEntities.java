package net.vakror.soulbound.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.ModItems;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SoulboundMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<BroomEntity>> BROOM = register("broom", (entityType, level) -> new BroomEntity(new ItemStack(ModItems.WAND.get()), level));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.EntityFactory<T> entityFactory) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, MobCategory.MISC).sized(1f, 1f).build(name));
    }
}
