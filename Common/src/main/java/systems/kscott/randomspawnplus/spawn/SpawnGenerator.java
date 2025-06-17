package systems.kscott.randomspawnplus.spawn;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.platforms.UniversalPlatform;
import systems.kscott.randomspawnplus.util.PlatformUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SpawnGenerator {

    public static void init() {
        initSpawnChunks();

        /* Setup safeblocks */
        List<String> unsafeBlockStrings;
        //unsafeBlockStrings = config.getStringList("unsafe-blocks");

        //unsafeBlocks = new ArrayList<>();
        //for (String string : unsafeBlockStrings) {
        //    unsafeBlocks.add(Material.matchMaterial(string));
        //}
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

        prepareChunksTask.thenAccept($ -> {
            System.out.println("Prepare chunks took " + (System.currentTimeMillis() - start) + "ms");
            UniversalPlatform.finalizeSpawnChunksGeneration();
        });
    }

    private static void initSpawnPoints() {

    }
}
