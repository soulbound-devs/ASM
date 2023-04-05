package net.vakror.asm.world.dimension;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.vakror.asm.ASMMod;
import net.vakror.asm.blocks.entity.custom.DungeonAccessBlockEntity;
import net.vakror.asm.mixin.IMinecraftServerAccessor;
import net.vakror.asm.packets.PacketSyncDimensionListChanges;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class DimensionUtils {
    public static boolean isDimensionWithUUID(DungeonAccessBlockEntity blockEntity) {
        return getDimension(blockEntity.getDimensionUUID()) == null;
    }

    public static Level getDimension(UUID uuid) {
        ResourceLocation id = new ResourceLocation(ASMMod.MOD_ID, "dungeon_" + uuid.toString());
        ResourceKey<Level> type = ResourceKey.create(Registry.DIMENSION_REGISTRY, id);
        return ServerLifecycleHooks.getCurrentServer().getLevel(type);
    }

    @SuppressWarnings("all")
    public static ServerLevel createWorld(Level world, DungeonAccessBlockEntity entity) {
        UUID uuid = entity.getDimensionUUID();
        ResourceLocation id = new ResourceLocation(ASMMod.MOD_ID, "dungeon_" + uuid.toString());

        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, id);
//        ResourceKey<DimensionType> type = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, key.location());
        ResourceKey<DimensionType> type = Dimensions.DUNGEON_TYPE;

        RegistryAccess registryAccess = Objects.requireNonNull(world.getServer()).registryAccess();

        return getOrCreateLevel(world.getServer(), key,
                (server, registryKey) -> {
                    ChunkGenerator generator = new FlatLevelSource(
                            registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY)
                            , new FlatLevelGeneratorSettings(Optional.empty(), BuiltinRegistries.BIOME));
                    ASMMod.LOGGER.info("create world func");
                    LevelStem stem = new LevelStem(registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getOrCreateHolderOrThrow(type), generator);
                    GameData.unfreezeData();
                    return stem;
                });
    }

    public static ServerLevel getOrCreateLevel(final MinecraftServer server, final ResourceKey<Level> levelKey, final BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        // (we're doing the lookup this way because we'll need the map if we need to add a new level)
        @SuppressWarnings("deprecation") // forgeGetWorldMap is deprecated because it's a forge-internal-use-only method
        final Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();

        // if the level already exists, return it
        final ServerLevel existingLevel = map.get(levelKey);
        if (existingLevel != null) {
            return existingLevel;
        }

        ASMMod.LOGGER.info("create level func");
        return createAndRegisterWorldAndDimension(server, map, levelKey, dimensionFactory);
    }

    @SuppressWarnings("deprecation") // because we call the forge internal method server#markWorldsDirty
    private static ServerLevel createAndRegisterWorldAndDimension(final MinecraftServer server, final Map<ResourceKey<Level>, ServerLevel> map, final ResourceKey<Level> worldKey, final BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        // get everything we need to create the dimension and the level
        final ServerLevel overworld = server.getLevel(Level.OVERWORLD);

        // dimension keys have a 1:1 relationship with level keys, they have the same IDs as well
        final ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, worldKey.location());
        final LevelStem dimension = dimensionFactory.apply(server, dimensionKey);

        // the int in create() here is radius of chunks to watch, 11 is what the server uses when it initializes worlds
        final ChunkProgressListener chunkProgressListener = ((IMinecraftServerAccessor) server).getProgressListenerFactory().create(11);
        final Executor executor = ((IMinecraftServerAccessor) server).getExecutor();
        final LevelStorageSource.LevelStorageAccess anvilConverter = ((IMinecraftServerAccessor) server).getStorageSource();
        final WorldData worldData = server.getWorldData();
        final WorldGenSettings worldGenSettings = worldData.worldGenSettings();
        final DerivedLevelData derivedLevelData = new DerivedLevelData(worldData, worldData.overworldData());

        // now we have everything we need to create the dimension and the level
        // this is the same order server init creates levels:
        // the dimensions are already registered when levels are created, we'll do that first
        // then instantiate level, add border listener, add to map, fire world load event

        // register the actual dimension
//        Registry.register(worldGenSettings.dimensions(), dimensionKey, dimension);
        Registry<LevelStem> dimensionRegistry = worldGenSettings.dimensions();
        if (dimensionRegistry instanceof WritableRegistry<LevelStem> writableRegistry) {
            writableRegistry.register(dimensionKey, dimension, Lifecycle.stable());
        } else {
            throw new IllegalStateException("Unable to register dimension '" + dimensionKey.location() + "'! Registry not writable!");
        }

        // create the world instance
        final ServerLevel newWorld = new ServerLevel(
                server,
                executor,
                anvilConverter,
                derivedLevelData,
                worldKey,
                dimension,
                chunkProgressListener,
                worldGenSettings.isDebug(),
                net.minecraft.world.level.biome.BiomeManager.obfuscateSeed(worldGenSettings.seed()),
                ImmutableList.of(), // "special spawn list"
                // phantoms, travelling traders, patrolling/sieging raiders, and cats are overworld special spawns
                // this is always empty for non-overworld dimensions (including json dimensions)
                // these spawners are ticked when the world ticks to do their spawning logic,
                // mods that need "special spawns" for their own dimensions should implement them via tick events or other systems
                false // "tick time", true for overworld, always false for nether, end, and json dimensions
        );

        // add world border listener, for parity with json dimensions
        // the vanilla behaviour is that world borders exist in every dimension simultaneously with the same size and position
        // these border listeners are automatically added to the overworld as worlds are loaded, so we should do that here too
        // TODO if world-specific world borders are ever added, change it here too
        overworld.getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(newWorld.getWorldBorder()));

        // register level
        map.put(worldKey, newWorld);

        // update forge's world cache so the new level can be ticked
        server.markWorldsDirty();

        // fire world load event
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(newWorld));

        // update clients' dimension lists
        PacketSyncDimensionListChanges.updateClientDimensionLists(ImmutableSet.of(worldKey), ImmutableSet.of());

        ASMMod.LOGGER.info("create and register dim func");
        return newWorld;
    }
}
