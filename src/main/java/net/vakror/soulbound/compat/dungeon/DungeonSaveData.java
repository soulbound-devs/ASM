package net.vakror.soulbound.compat.dungeon;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.blocks.custom.DungeonAccessBlock;
import net.vakror.soulbound.compat.dungeon.dimension.Dimensions;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistry;
import net.vakror.soulbound.compat.dungeon.setup.DungeonSetup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DungeonSaveData {
    public static class DungeonLevelsListSaveData extends SavedData {
        public final List<ResourceLocation> loadedDungeonLevels = new ArrayList<>();
        public static DungeonLevelsListSaveData INSTANCE = new DungeonLevelsListSaveData();

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
            ListTag levels = new ListTag();
            for (ResourceLocation loadedDungeonLevel : loadedDungeonLevels) {
                levels.add(StringTag.valueOf(loadedDungeonLevel.toString()));
            }
            compoundTag.put("dungeonLevels", levels);
            return compoundTag;
        }


        public static DungeonLevelsListSaveData load(CompoundTag compoundTag) {
            if (compoundTag.get("dungeonLevels") != null) {
                DungeonLevelsListSaveData instance = new DungeonLevelsListSaveData();
                ListTag levels = (ListTag) compoundTag.get("dungeonLevels");
                assert levels != null;
                for (Tag level : levels) {
                    StringTag tag = (StringTag) level;
                    instance.loadedDungeonLevels.add(new ResourceLocation(tag.getAsString()));
                }
                return instance;
            }
            return null;
        }

        public static void init(ServerLevel overworld) {
            DimensionDataStorage storage = overworld.getDataStorage();

            INSTANCE = storage.computeIfAbsent(new Factory<>(DungeonLevelsListSaveData::new, DungeonLevelsListSaveData::load), SoulboundMod.MOD_ID + "_dungeons");
        }
    }

    @Mod.EventBusSubscriber(modid = SoulboundMod.MOD_ID)
    public static class SavedDataEvents {
        @SubscribeEvent
        public static void loadDungeonData(LevelEvent.Load event) {
            if (!event.getLevel().isClientSide()) {
                ServerLevel overworld = event.getLevel().getServer().overworld();
                if (event.getLevel().equals(overworld)) {
                    DungeonLevelsListSaveData.init(overworld);
                    for (ResourceLocation loadedDungeonLevel : DungeonLevelsListSaveData.INSTANCE.loadedDungeonLevels) {
                        DungeonAccessBlock.createDimension(loadedDungeonLevel, overworld);
                    }
                }
            }
        }
    }
}
