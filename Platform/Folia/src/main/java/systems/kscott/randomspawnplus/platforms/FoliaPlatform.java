package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public class FoliaPlatform extends PaperPlatform {

    @Override
    public CompletableFuture<LongArrayList> collectNonGeneratedChunksAsync(World level, int minX, int minZ, int maxX, int maxZ) {
        // TODO: check if work on folia and remove it.
        return super.collectNonGeneratedChunksAsync(level, minX, minZ, maxX, maxZ);
    }

    @Override
    public void broadcastCommandMessage(CommandSender sender, Object message) {
        throw new UnsupportedOperationException();
    }
}
