package systems.kscott.randomspawnplus.spawn;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpawnData {

    // Spawning status
    private static LongArrayList pendingChunksForGen;
    private static boolean isAllSpawnRangeChunksGenerated;

    // Unsafe blocks
    private static Set<Material> unsafeBlocks;

    public static final List<String> cachedSpawns = new ArrayList<>();
    public static WrappedTask cacheSpawnTask;

    // Check whether need to use method, or public field to access directly.
    // Or move to interface as default?
    public static void setPendingGenerateChunksList(LongArrayList chunks) {
        pendingChunksForGen = chunks;
    }

    public static LongArrayList getPendingGenerateChunksList() {
        return pendingChunksForGen;
    }

    public static void finalizeSpawnChunksGeneration() {
        isAllSpawnRangeChunksGenerated = true;
    }

    public static boolean isAllSpawnRangeChunksGenerated() {
        return isAllSpawnRangeChunksGenerated;
    }

    public static Set<Material> getUnsafeBlocks() {
        return unsafeBlocks;
    }

    public static void setUnsafeBlocks(Set<Material> blocks) {
        unsafeBlocks = blocks;
    }
}
