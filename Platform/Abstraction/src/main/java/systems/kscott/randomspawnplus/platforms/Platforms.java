package systems.kscott.randomspawnplus.platforms;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

public interface Platforms {

    CompletableFuture<LongArrayList> collectNonGeneratedChunksAsync(World level, int minX, int minZ, int maxX, int maxZ);

    void broadcastCommandMessage(CommandSender sender, Object message);
}
