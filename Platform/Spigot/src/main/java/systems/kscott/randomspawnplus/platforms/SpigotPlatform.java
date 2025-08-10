package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class SpigotPlatform implements Platforms {

    @Override
    public CompletableFuture<LongArrayList> collectNonGeneratedChunksAsync(World level, int minX, int minZ, int maxX, int maxZ) {
            throw new IllegalStateException("SpigotPlatform does not support loadNonGeneratedChunksAsync");
//        return CompletableFuture.runAsync(
//                 () -> collectNonGeneratedChunks(level, minX, minZ, maxX, maxZ)
//        );
    }

    private LongArrayList collectNonGeneratedChunks(World level, int minX, int minZ, int maxX, int maxZ) {
        final int minChunkX = minX >> 4;
        final int minChunkZ = minZ >> 4;
        final int maxChunkX = maxX >> 4;
        final int maxChunkZ = maxZ >> 4;

        final LongArrayList chunks = new LongArrayList();

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                final Chunk chunk = level.getChunkAt(chunkX, chunkZ, true);

                if (!chunk.isGenerated()) {
                    final long chunkKey = (long) chunkX & 0xffffffffL | ((long) chunkZ & 0xffffffffL) << 32;
                    chunks.add(chunkKey);
                }
            }
        }

        return chunks;
    }

    @Override
    public void broadcastCommandMessage(CommandSender sender, Object message) {
        throw new UnsupportedOperationException();
    }
}
