package systems.kscott.randomspawnplus.spawn;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.jetbrains.annotations.Nullable;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.events.SpawnCheckEvent;
import systems.kscott.randomspawnplus.util.Locations;
import systems.kscott.randomspawnplus.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnFinder {

    public static Location getRandomSpawn() throws Exception {
        boolean valid = false;
        Location location = null;

        int tries = 0;
        while (!valid) {
            // TODO: configure here
            if (tries >= 30) {
                throw new Exception();
            }
            if (SpawnData.cachedSpawns.isEmpty()) {
                String msg = Config.getLangConfig().noSpawnFound;
                RandomSpawnPlus.getInstance().getLogger().severe(msg);
            }
            if (!SpawnData.cachedSpawns.isEmpty()) {
                location = getRandomSpawn2();
            } else {
                location = getCandidateLocation();
            }
            valid = checkSpawn(location);

            if (!valid) {
                deleteSpawn(location);
            }
            tries++;
        }

        return location.add(0.5, 1, 0.5);
    }

    @Nullable
    public static Location getCandidateLocation() {
        String worldString = Config.getGlobalConfig().respawnWorld;

        if (worldString == null) {
            RandomSpawnPlus.getInstance().getLogger().severe("You've incorrectly defined the `respawn-world` key in the config.");
            RandomSpawnPlus.getInstance().getServer().getPluginManager().disablePlugin(RandomSpawnPlus.getInstance());
            return null;
        }

        World world = Bukkit.getWorld(worldString);

        if (world == null) {
            RandomSpawnPlus.getInstance().getLogger().severe("The world '" + worldString + "' is invalid. Please change the 'respawn-world' key in the config.");
            RandomSpawnPlus.getInstance().getServer().getPluginManager().disablePlugin(RandomSpawnPlus.getInstance());
            return null;
        }

        int minX = Config.getGlobalConfig().spawnRangeMinX;
        int minZ = Config.getGlobalConfig().spawnRangeMinZ;
        int maxX = Config.getGlobalConfig().spawnRangeMaxX;
        int maxZ = Config.getGlobalConfig().spawnRangeMaxZ;

        if (Config.getGlobalConfig().blockedSpawnZoneEnabled) {
            final int blockedMinX = Config.getGlobalConfig().blockedSpawnZoneMinX;
            final int blockedMinZ = Config.getGlobalConfig().blockedSpawnZoneMinZ;
            final int blockedMaxX = Config.getGlobalConfig().blockedSpawnZoneMaxX;
            final int blockedMaxZ = Config.getGlobalConfig().blockedSpawnZoneMaxZ;

            SpawnRegion region1 = new SpawnRegion(minX, blockedMinX, minZ, blockedMinZ);
            SpawnRegion region2 = new SpawnRegion(blockedMinX, blockedMaxX, blockedMaxZ, maxZ - blockedMaxZ);
            SpawnRegion region3 = new SpawnRegion(blockedMaxX, maxX, blockedMaxZ, maxX);
            SpawnRegion region4 = new SpawnRegion(blockedMinZ, maxZ - blockedMinZ, minZ + blockedMinX, maxZ - blockedMinZ);

            SpawnRegion[] spawnRegions = new SpawnRegion[]{region1, region2, region3, region4};

            SpawnRegion region = spawnRegions[ThreadLocalRandom.current().nextInt(3)];

            minX = region.getMinX();
            minZ = region.getMinZ();
            maxX = region.getMaxX();
            maxZ = region.getMaxZ();
        }

        int candidateX = Util.getRandomNumberInRange(minX, maxX);
        int candidateZ = Util.getRandomNumberInRange(minZ, maxZ);
        int candidateY = getHighestY(world, candidateX, candidateZ);

        return new Location(world, candidateX, candidateY, candidateZ);
    }

    public static boolean checkSpawn(Location location) {
        if (location == null) return false;

        final boolean blockedSpawnRange = Config.getGlobalConfig().blockedSpawnZoneEnabled;

        final int blockedMinX = Config.getGlobalConfig().blockedSpawnZoneMinX;
        final int blockedMinZ = Config.getGlobalConfig().blockedSpawnZoneMinZ;
        final int blockedMaxX = Config.getGlobalConfig().blockedSpawnZoneMaxX;
        final int blockedMaxZ = Config.getGlobalConfig().blockedSpawnZoneMaxZ;

        boolean isValid;

        Location locClone = location.clone();

        if (!location.getChunk().isLoaded()) {
            return false;
        }

        Block block0 = locClone.getBlock();
        Block block1 = locClone.add(0, 1, 0).getBlock();
        Block block2 = locClone.add(0, 1, 0).getBlock();

        SpawnCheckEvent spawnCheckEvent = new SpawnCheckEvent(location);

        Bukkit.getServer().getPluginManager().callEvent(spawnCheckEvent);

        isValid = spawnCheckEvent.isValid();

        if (blockedSpawnRange) {
            if (Util.betweenExclusive((int) location.getX(), blockedMinX, blockedMaxX)) {
                isValid = false;
            }
            if (Util.betweenExclusive((int) location.getZ(), blockedMinZ, blockedMaxZ)) {
                isValid = false;
            }
        }

        if (block0.getType().isAir()) {
            isValid = false;
        }

        if (!block1.getType().isAir() || !block2.getType().isAir()) {
            isValid = false;
        }

        if (SpawnData.getUnsafeBlocks() != null && SpawnData.getUnsafeBlocks().contains(block1.getType())) {
            isValid = false;
        }

        // TODO: detect unsafe-blocks contains water
        if (true && block0.getType() == Material.WATER) {
            isValid = false;
        }

        // TODO: detect unsafe-blocks contains lava
        if (true && block0.getType() == Material.LAVA) {
            isValid = false;
        }

        return isValid;
    }

    public static Location getRandomSpawn2() {
        int element = ThreadLocalRandom.current().nextInt(SpawnData.cachedSpawns.size());
        return Locations.deserializeLocationString(SpawnData.cachedSpawns.get(element));
    }

    public static void deleteSpawn(Location location) {
        SpawnData.cachedSpawns.removeIf(locationString -> Locations.serializeString(location).equals(locationString));
        Config.getSpawnStorage().get().set("spawns", SpawnData.cachedSpawns);
        try {
            Config.getSpawnStorage().saveConfig();
        } catch (IOException e) {
            RandomSpawnPlus.LOGGER.error("Failed to save " + Config.SPAWN_STORAGE_FILE_NAME + "!", e);
        }
    }

    public static int getHighestY(World world, int x, int z) {
        int maxHeight = world.getMaxHeight();
        int minHeight = world.getMinHeight();

        for (int i = maxHeight; i >= minHeight; i--) {
            Location location = new Location(world, x, i, z);
            if (!location.getBlock().isEmpty()) {
                return i;
            }
        }
        return minHeight;
    }
}
