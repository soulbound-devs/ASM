package net.vakror.soulbound.compat.dungeon;

import com.google.common.collect.Lists;
import net.commoble.infiniverse.internal.QuietPacketDistributors;
import net.commoble.infiniverse.internal.ReflectionBuddy;
import net.commoble.infiniverse.internal.UpdateDimensionsPacket;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.dungeon.attachment.DungeonAttachment;
import net.vakror.soulbound.compat.dungeon.blocks.ModDungeonBlocks;
import net.vakror.soulbound.compat.dungeon.registry.DungeonRegistry;
import net.vakror.soulbound.compat.dungeon.setup.DungeonSetup;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class DungeonUtils {
    static boolean generateDungeon(TickEvent.LevelTickEvent event, DungeonAttachment dungeonData) {
        DungeonSetup.State state = DungeonRegistry.dungeons.get(dungeonData.getDungeon().type).placer().place((ServerLevel) event.level, event.level.getServer().registryAccess());
        if (state.equals(DungeonSetup.State.FAIL)) {
            throw new IllegalStateException("Failed To Generate Dungeon", DungeonSetup.State.FAIL.getException());
        } else if (state.equals(DungeonSetup.State.RETRY)) {
            return false;
        }
        DungeonSetup.State.FAIL.setException(null);
        return true;
    }

    static void setupDungeon(TickEvent.LevelTickEvent event, DungeonAttachment dungeonData) {
        placeReturnBlockUnderneathEachPlayer(event);
    }

    private static void placeReturnBlockUnderneathEachPlayer(TickEvent.LevelTickEvent event) {
        for (Player player : event.level.players()) {
            event.level.setBlock(player.blockPosition().below(), ModDungeonBlocks.RETURN_TO_OVERWORLD_BLOCK.get().defaultBlockState(), 3);
        }
    }

    public static final Set<ResourceKey<Level>> dungeonsToDelete = new LinkedHashSet<>();

    public static void deleteWorld(MinecraftServer server) {
        remove(server);
    }

    private static void remove(final MinecraftServer server) {
        if (dungeonsToDelete.isEmpty())
            return;

        // flush the buffer
        final Set<ResourceKey<Level>> keysToRemove = dungeonsToDelete;

        // we need to remove the dimension/level from three places:
        // the server's dimension/levelstem registry, the server's level registry, and
        // the overworld's border listener
        // the level registry is just a simple map and the border listener has a remove() method
        // the dimension registry has five sub-collections that need to be cleaned up
        // we should also eject players from removed worlds so they don't get stuck there

        final Registry<LevelStem> oldRegistry = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        if (!(oldRegistry instanceof MappedRegistry<LevelStem> oldMappedRegistry)) {
            SoulboundMod.LOGGER.warn("Cannot unload dimensions: dimension registry not an instance of MappedRegistry. There may be another mod causing incompatibility with Infiniverse, or Infiniverse may need to be updated for your version of forge/minecraft.");
            return;
        }
        LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess = ReflectionBuddy.MinecraftServerAccess.registries.apply(server);
        RegistryAccess.Frozen composite = ReflectionBuddy.LayeredRegistryAccessAccess.composite.apply(layeredRegistryAccess);
        if (!(composite instanceof RegistryAccess.ImmutableRegistryAccess immutableRegistryAccess)) {
            SoulboundMod.LOGGER.warn("Cannot unload dimensions: composite registry not an instance of ImmutableRegistryAccess. There may be another mod causing incompatibility with Infiniverse, or Infiniverse may be updated for your version of forge/minecraft.");
            return;
        }

        final Set<ResourceKey<Level>> removedLevelKeys = new HashSet<>();
        final ServerLevel overworld = server.getLevel(Level.OVERWORLD);

        for (final ResourceKey<Level> levelKeyToRemove : keysToRemove) {
            final @Nullable ServerLevel levelToRemove = server.getLevel(levelKeyToRemove);
            if (levelToRemove == null)
                continue;

            // null if specified level not present
            final @Nullable ServerLevel removedLevel = server.forgeGetWorldMap().remove(levelKeyToRemove);

            if (removedLevel != null) // if we removed the key from the map
            {
                // eject players from dead world
                // iterate over a copy as the world will remove players from the original list
                ejectPlayers(removedLevel, keysToRemove, overworld, server);

                // fire world unload event -- when the server stops, this would fire after
                // worlds get saved, we'll do that here too
                NeoForge.EVENT_BUS.post(new LevelEvent.Unload(removedLevel));

                // remove the world border listener if possible
                removeLevelFromBorderListener(server, levelToRemove, overworld, removedLevel);

                // track the removed level
                removedLevelKeys.add(levelKeyToRemove);
            }
        }

        if (!removedLevelKeys.isEmpty()) {
            // replace the old dimension registry with a new one containing the dimensions
            // that weren't removed, in the same order
            final MappedRegistry<LevelStem> newRegistry = new MappedRegistry<>(Registries.LEVEL_STEM, oldMappedRegistry.registryLifecycle());

            for (final var entry : oldRegistry.entrySet()) {
                final ResourceKey<LevelStem> oldKey = entry.getKey();
                final ResourceKey<Level> oldLevelKey = ResourceKey.create(Registries.DIMENSION, oldKey.location());
                final LevelStem dimension = entry.getValue();
                if (oldKey != null && dimension != null && !removedLevelKeys.contains(oldLevelKey)) {
                    newRegistry.register(oldKey, dimension, oldRegistry.lifecycle(dimension));
                }
            }

            // then replace the old registry with the new registry
            // as of 1.20.1 the dimension registry is stored in the server's layered registryaccess
            // this has several immutable collections of sub-registryaccesses,
            // so we'll need to recreate each of them.

            // Each ServerLevel has a reference to the layered registry access's *composite* registry access
            // so we should edit the internal fields where possible (instead of reconstructing the registry accesses)

            List<RegistryAccess.Frozen> newRegistryAccessList = new ArrayList<>();
            for (RegistryLayer layer : RegistryLayer.values()) {
                if (layer == RegistryLayer.DIMENSIONS) {
                    newRegistryAccessList.add(new RegistryAccess.ImmutableRegistryAccess(List.of(newRegistry)).freeze());
                } else {
                    newRegistryAccessList.add(layeredRegistryAccess.getLayer(layer));
                }
            }
            Map<ResourceKey<? extends Registry<?>>, Registry<?>> newRegistryMap = new HashMap<>();
            for (var registryAccess : newRegistryAccessList) {
                var registries = registryAccess.registries().toList();
                for (var registryEntry : registries) {
                    newRegistryMap.put(registryEntry.key(), registryEntry.value());
                }
            }
            ReflectionBuddy.LayeredRegistryAccessAccess.values.set(layeredRegistryAccess, List.copyOf(newRegistryAccessList));
            ReflectionBuddy.ImmutableRegistryAccessAccess.registries.set(immutableRegistryAccess, newRegistryMap);

            // update the server's levels so dead levels don't get ticked
            server.markWorldsDirty();

            // notify client of the removed levels
            QuietPacketDistributors.sendToAll(server, new UpdateDimensionsPacket(removedLevelKeys, false));
        }
        Path dimensionPath = server.getWorldPath(LevelResource.ROOT).resolve("dimensions");
        for (ResourceKey<Level> key : dungeonsToDelete) {
            Path dimPath = dimensionPath.resolve(key.location().getNamespace() + "/" + key.location().getPath());
            try {
                FileUtils.deleteDirectory(dimPath.toFile());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Deleted " + dimPath);
        }
        dungeonsToDelete.clear();
    }

    private static void removeLevelFromBorderListener(MinecraftServer server, ServerLevel levelToRemove, ServerLevel overworld, ServerLevel removedLevel) {
        final WorldBorder overworldBorder = overworld.getWorldBorder();
        final WorldBorder removedWorldBorder = removedLevel.getWorldBorder();
        final List<BorderChangeListener> listeners = ReflectionBuddy.WorldBorderAccess.listeners.apply(overworldBorder);
        BorderChangeListener targetListener = null;
        for (BorderChangeListener listener : listeners) {
            if (listener instanceof BorderChangeListener.DelegateBorderChangeListener delegate
                    && removedWorldBorder == ReflectionBuddy.DelegateBorderChangeListenerAccess.worldBorder.apply(delegate)) {
                targetListener = listener;
                break;
            }
        }
        if (targetListener != null) {
            overworldBorder.removeListener(targetListener);
        }
    }


    private static void ejectPlayers(ServerLevel removedLevel, Set<ResourceKey<Level>> keysToRemove, ServerLevel overworld, MinecraftServer server) {
        for (final ServerPlayer player : Lists.newArrayList(removedLevel.players())) {
            // send players to their respawn point
            ResourceKey<Level> respawnKey = player.getRespawnDimension();
            // if we're removing their respawn world then just send them to the overworld
            if (keysToRemove.contains(respawnKey)) {
                respawnKey = Level.OVERWORLD;
                player.setRespawnPosition(respawnKey, null, 0, false, false);
            }
            if (respawnKey == null) {
                respawnKey = Level.OVERWORLD;
            }
            @Nullable ServerLevel destinationLevel = server.getLevel(respawnKey);
            if (destinationLevel == null) {
                destinationLevel = overworld;
            }

            @Nullable
            BlockPos destinationPos = player.getRespawnPosition();
            if (destinationPos == null) {
                destinationPos = destinationLevel.getSharedSpawnPos();
            }

            final float respawnAngle = player.getRespawnAngle();
            // "respawning" the player via the player list schedules a task in the server to
            // run after the post-server tick
            // that causes some minor logspam due to the player's world no longer being
            // loaded
            // teleporting the player via a teleport avoids this
            player.teleportTo(destinationLevel, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), respawnAngle, 0F);
        }
    }

}
