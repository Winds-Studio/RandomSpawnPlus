package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PaperPlatform implements Platforms {

    @Override
    public CompletableFuture<LongArrayList> collectNonGeneratedChunksAsync(World level, int minX, int minZ, int maxX, int maxZ) {
        return collectNonGeneratedChunks(level, minX, minZ, maxX, maxZ);
    }

    private static final AtomicInteger counter = new AtomicInteger(0);
    private CompletableFuture<LongArrayList> collectNonGeneratedChunks(World level, int minX, int minZ, int maxX, int maxZ) {
        int minChunkX = minX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkX = maxX >> 4;
        int maxChunkZ = maxZ >> 4;

        LongArrayList chunks = new LongArrayList();
        LongArrayList nonGeneratedChunksTotal = new LongArrayList();
        List<CompletableFuture<LongArrayList>> futures = new ArrayList<>();
        int parallelThreads = Runtime.getRuntime().availableProcessors() * 4;

        // Collect all chunks into one list
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                long chunkKey = Chunk.getChunkKey(chunkX, chunkZ);
                chunks.add(chunkKey);
            }
        }

        int partitionSize = chunks.size() / parallelThreads;

        for (int i = 0; i < chunks.size(); i += partitionSize) {
            int end = Math.min(chunks.size(), i + partitionSize);
            LongList partitionList = chunks.subList(i, end);
            CompletableFuture<LongArrayList> partitionChunksCollectTask = checkChunksGenerated(level, partitionList);

            partitionChunksCollectTask.thenAccept(nonGeneratedChunksTotal::addAll);
            futures.add(partitionChunksCollectTask);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply($ -> nonGeneratedChunksTotal);
    }

    private static CompletableFuture<LongArrayList> checkChunksGenerated(World level, LongList partitionChunks) {
        LongArrayList nonGeneratedChunks = new LongArrayList();

        return CompletableFuture.supplyAsync(() -> {
            int count = 0;
                    for (long chunkKey : partitionChunks) {
                        int chunkX = (int) chunkKey;
                        int chunkZ = (int) (chunkKey >> 32);

                        counter.incrementAndGet();
                        if (count++ == 1000) {
                            System.out.println(counter.get());
                            System.out.println("NON chunks size: " + nonGeneratedChunks.size());
                            count = 0;
                        }

                        if (!level.isChunkGenerated(chunkX, chunkZ)) {
                            nonGeneratedChunks.add(chunkKey);
                        }
                    }

                    return nonGeneratedChunks;
                }
        );
    }

    @Override
    public void broadcastCommandMessage(CommandSender sender, Object message) {
        throw new UnsupportedOperationException();
    }
}
