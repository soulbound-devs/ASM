package net.vakror.asm.entity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vakror.asm.ASMMod;
import net.vakror.asm.items.ModItems;
import org.jetbrains.annotations.NotNull;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ASMMod.MOD_ID);

    public static final RegistryObject<EntityType<BroomEntity>> BROOM = register("broom", (entityType, level) -> {
        return new BroomEntity(new ItemStack(ModItems.WAND.get()), level);
    });

    public static final RegistryObject<EntityType<GoblaggerEntity>> GOBLAGGER = registerDungeonMob("goblagger", GoblaggerEntity::new);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.EntityFactory<T> entityFactory) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entityFactory, MobCategory.MISC).sized(1f, 1f).build(name));
    }
    public static <T extends Entity> RegistryObject<EntityType<T>> registerDungeonMob(String name, EntityType.EntityFactory<T> entityFactory) {
        return ENTITIES.register(name, () -> new EntityType<T>(entityFactory, MobCategory.MONSTER, true, true, true, true, ImmutableSet.of(), EntityDimensions.scalable(1f, 1f), 20, 1, FeatureFlags.VANILLA_SET) {
            @Override
            public boolean isBlockDangerous(@NotNull BlockState state) {
                return false;
            }

            @Override
            public boolean fireImmune() {
                return super.fireImmune();
            }
        });
    }
}
