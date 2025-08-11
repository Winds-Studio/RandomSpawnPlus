package systems.kscott.randomspawnplus.spawn;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.popcraft.chunky.api.ChunkyAPI;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.hooks.HookInstance;
import systems.kscott.randomspawnplus.util.Locations;
import systems.kscott.randomspawnplus.util.PlatformUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SpawnGenerator {

    public static void init() {
        initUnsafeBlocks();
        initSpawnChunks();
    }

    private static void initUnsafeBlocks() {
        List<String> unsafeBlockStrings = Config.getGlobalConfig().unsafeBlocks;

        if (unsafeBlockStrings.isEmpty()) return;

        SpawnData.setUnsafeBlocks(new HashSet<>());

        for (String str : unsafeBlockStrings) {
            // TODO: use XMaterial here
            Material material = Material.matchMaterial(str);
            SpawnData.getUnsafeBlocks().add(material);
        }
    }

    private static void initSpawnChunks() {
        String worldStr = Config.getGlobalConfig().respawnWorld;
        World spawnLevel = Bukkit.getWorld(worldStr);
        int minX = Config.getGlobalConfig().spawnRangeMinX;
        int minZ = Config.getGlobalConfig().spawnRangeMinZ;
        int maxX = Config.getGlobalConfig().spawnRangeMaxX;
        int maxZ = Config.getGlobalConfig().spawnRangeMaxZ;

        long start = System.currentTimeMillis();
        CompletableFuture<LongArrayList> prepareChunksTask = PlatformUtil.getPlatform().collectNonGeneratedChunksAsync(spawnLevel, minX, minZ, maxX, maxZ);

        prepareChunksTask.thenAccept(chunks -> {
            if (chunks.isEmpty()) return;

            System.out.println("Found non-generated chunks: " + chunks.size());
            System.out.println("Prepare chunks took " + (System.currentTimeMillis() - start) + "ms");

            generateSpawnChunks(worldStr, minX, minZ, maxX, maxZ);
        });
    }

    private static void generateSpawnChunks(String spawnLevel, int minX, int minZ, int maxX, int maxZ) {
        ChunkyAPI chunky = RandomSpawnPlus.getHooks().getChunky();
        chunky.startTask(spawnLevel, "square", minX, minZ, maxX, maxZ, "concentric");
        chunky.onGenerationComplete(event -> {
            RandomSpawnPlus.LOGGER.info("Generation completed for {}", event.world());
            SpawnData.finalizeSpawnChunksGeneration();
            initSpawnPoints();
        });
    }

    private static void initSpawnPoints() {
        List<String> locationStrings = Config.getSpawnStorage().get().getStringList("spawns");

        SpawnData.cachedSpawns.addAll(locationStrings);

        int missingLocations = Config.getGlobalConfig().spawnCacheCount - locationStrings.size();

        if (missingLocations <= 0) {
            return;
        }

        List<String> newLocations = new ArrayList<>();

        Bukkit.getLogger().info("Caching " + missingLocations + " spawns.");
        for (int i = 0; i <= missingLocations; i++) {
            RandomSpawnPlus.getInstance().foliaLib.getScheduler().runLater(() -> {
                Location location = null;
                boolean valid = false;

                while (!valid) {
                    location = SpawnFinder.getCandidateLocation();
                    valid = SpawnFinder.checkSpawn(location);
                }

                newLocations.add(Locations.serializeString(location));
            }, 1);
        }

        SpawnData.cacheSpawnTask = RandomSpawnPlus.getInstance().foliaLib.getScheduler().runTimer(() -> {
            // Wait for all spawns to be cached
            if (newLocations.size() > missingLocations) {
                SpawnData.cachedSpawns.addAll(newLocations);
                // Save spawns to file
                Config.getSpawnStorage().get().set("spawns", SpawnData.cachedSpawns);
                RandomSpawnPlus.getInstance().saveConfig();

                RandomSpawnPlus.getInstance().foliaLib.getScheduler().cancelTask(SpawnData.cacheSpawnTask);
            }
        }, 10, 10);
    }
}
