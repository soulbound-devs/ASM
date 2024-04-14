package net.vakror.soulbound.compat.dungeon;

import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.blocks.custom.DungeonAccessBlock;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class DungeonSaveData {
    public static class DungeonLevelsListSaveData extends SavedData {
        public final Set<ResourceLocation> loadedDungeonLevels = new LinkedHashSet<>();
        public static DungeonLevelsListSaveData INSTANCE = new DungeonLevelsListSaveData();

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
            for (ServerLevel level : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
                if (loadedDungeonLevels.contains(level.dimension().location())) {
                    if (!level.players().isEmpty()) {
                        ListTag levels = new ListTag();
                        levels.addAll(loadedDungeonLevels.stream().map(location -> StringTag.valueOf(location.toString())).toList());
                        compoundTag.put("dungeonLevels", levels);
                    }
                }
            }
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
