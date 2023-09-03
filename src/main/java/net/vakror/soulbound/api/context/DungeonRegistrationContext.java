package net.vakror.soulbound.api.context;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.api.SoulboundApi;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.hammerspace.dungeon.level.DungeonLevel;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DefaultDungeonTypes;
import net.vakror.soulbound.mod.compat.hammerspace.structure.type.DungeonType;
import net.vakror.soulbound.mod.compat.hammerspace.structure.util.DungeonFileLocations;

/**
 * The default registration context for all dungeon things
 * Used to register levels, dungeon file locations, and types
 * Will only be registered if hammerspace is present
 */
public class DungeonRegistrationContext implements IRegistrationContext {

    /**
     * Registers a dungeon level
     *
     * @param dungeonLevel the level to register
     * @param size the size (horizontally; must be square) of your level
     * @param level the level for which to register this to
     */
    public void registerDungeonLevel(DungeonLevel dungeonLevel, int size, int level) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Checking If Dungeon Level {} Of Size {} And Level {} Can Be Registered", dungeonLevel.getName(), size, level);
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterDungeonLevel(dungeonLevel, size, level)) {
            SoulboundMod.LOGGER.info("Dungeon Level {} Of Size {} And Level {} CAN Be Registered, \033[0;31mChecks Took {}\033[0;0m", dungeonLevel.getName(), size, level, stopwatch2);
            if (level < 0) {
                SoulboundMod.LOGGER.error("Attempted to register dungeon level {} at size {} at a level below zero ({})", dungeonLevel.getName(), size, level);
            } else {
                SoulboundMod.LOGGER.info("Registering Dungeon Level {} of Size {} at level {}", dungeonLevel.getName(), size, level);
                DungeonLevel.ALL_LEVELS.put(new Pair<>(size, level), dungeonLevel);
                SoulboundMod.LOGGER.info("Registered Dungeon Level {} of Size {} at level {}, \033[0;31mTook {}\033[0;0m", dungeonLevel.getName(), size, level, stopwatch);
                SoulboundApi.onRegisterDungeonLevel(dungeonLevel, size, level);
            }
        } else {
            SoulboundMod.LOGGER.info("Dungeon Level {} Of Size {} And Level {} CANNOT Be Registered, \033[0;31mChecks Took {}\033[0;0m", dungeonLevel.getName(), size, level, stopwatch2);
        }
    }

    /**
     * registers a file location for a given level and size
     * multiple dungeon file locations can exist for a single level and size. a random one will be chosen at generation
     *
     * @param fileLocation the location pointing to your structure file
     * @param size the size (horizontally, must be square) of your structure
     * @param level the level of your structure
     */
    public void registerDungeonFileLocation(ResourceLocation fileLocation, int size, int level) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SoulboundMod.LOGGER.info("Checking If Dungeon File Location {} Of Size {} And Level {} Can Be Registered", fileLocation, size, level);
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterDungeonFileLocation(size, level, fileLocation)) {
            SoulboundMod.LOGGER.info("Dungeon File Location {} Of Size {} And Level {} CAN Be Registered, \033[0;31mChecks Took {}\033[0;0m", fileLocation, size, level, stopwatch2);
            if (level < 0) {
                SoulboundMod.LOGGER.error("Attempted to register dungeon file {} at size {} at a level below zero ({})", fileLocation, size, level);
            } else {
                SoulboundMod.LOGGER.info("Registering Dungeon File Location \"{}\" For Size {} and Level {}", fileLocation, size, level);
                DungeonFileLocations.FILES.put(new Pair<>(size, level), fileLocation);
                SoulboundMod.LOGGER.info("Registered Dungeon File Location \"{}\" For Size {} and Level {}, \033[0;31mTook {}\033[0;0m", fileLocation, size, level, stopwatch);
                SoulboundApi.onRegisterDungeonFileLocation(size, level, fileLocation);
            }
        } else {
            SoulboundMod.LOGGER.info("Dungeon File Location {} Of Size {} And Level {} CANNOT Be Registered, \033[0;31mChecks Took {}\033[0;0m", fileLocation, size, level, stopwatch2);
        }
    }

    /**
     * Registers a dungeon type
     *
     * @param type the dungeon type to register
     */
    public void registerDungeonType(DungeonType type) {
        SoulboundMod.LOGGER.info("Checking If Dungeon Type {} Can Be Registered", type.id());
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        if (SoulboundApi.canRegisterDungeonType(type)) {
            SoulboundMod.LOGGER.info("Dungeon Type {} CAN Be Registered, \033[0;31mChecks Took {}\033[0;0m", type.id(), stopwatch2);
            SoulboundMod.LOGGER.info("Registering Dungeon Type {}", type.id());
            Stopwatch stopwatch = Stopwatch.createStarted();
            DefaultDungeonTypes.ALL_DUNGEON_TYPES.add(type);
            SoulboundMod.LOGGER.info("Registering Dungeon Type {}, \033[0;31mTook {}\033[0;0m", type.id(), stopwatch);
            SoulboundApi.onRegisterDungeonType(type);
        } else {
            SoulboundMod.LOGGER.info("Dungeon Type {} CANNOT Be Registered, \033[0;31mChecks Took {}\033[0;0m", type.id(), stopwatch2);
        }
    }


    @Override
    public String getContextName() {
        return "default";
    }
}