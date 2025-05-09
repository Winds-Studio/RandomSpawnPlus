package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;

public class UniversalPlatform {

    // Spawning status
    private static LongArrayList pendingChunksForGen;
    private static boolean isAllSpawnRangeChunksGenerated;

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
}
