package systems.kscott.randomspawnplus.listeners;

import com.earth2me.essentials.User;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.platforms.UniversalPlatform;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class RSPFirstJoinListener implements Listener {

    private final FileConfiguration config;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void firstJoinHandler(PlayerJoinEvent event) {
        if (!config.getBoolean("randomspawn-enabled")) {
            return;
        }

        if (!config.getBoolean("on-first-join")) {
            return;
        }

        Player player = event.getPlayer();

        if (!RSPLoginListener.firstJoinPlayers.contains(player.getUniqueId())) {
            return;
        }

        if (config.getBoolean("use-permission-node") && !player.hasPermission("randomspawnplus.randomspawn")) {
            RSPLoginListener.firstJoinPlayers.remove(player.getUniqueId());
            return;
        }

        try {
            Location spawnLoc = SpawnFinder.getInstance().findSpawn(true);
            // quiquelhappy start - Prevent essentials home replace
            boolean prevent = false;

            if (config.getBoolean("essentials-home-on-first-spawn") && RandomSpawnPlus.getHooks().getEssentials() != null) {
                User user = RandomSpawnPlus.getHooks().getEssentials().getUser(player);
                if (!user.hasHome()) {
                    user.setHome("home", spawnLoc);
                    user.save();
                } else {
                    prevent = true;
                }
            }

            if (!prevent) {
                RandomSpawnPlus.getInstance().foliaLib.getImpl().runLater(() -> {
                    RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(spawnLoc, player, SpawnType.FIRST_JOIN);

                    Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
                    RandomSpawnPlus.getInstance().foliaLib.getImpl().teleportAsync(player, spawnLoc.add(0.5, 0, 0.5));
                }, 3);
            } else {
                RandomSpawnPlus.getInstance().getLogger().warning("The spawn finder prevented a teleport for " + player.getUniqueId() + ", since essentials sethome is enabled and the player already had a home (perhaps old player data?).");
            }
            // quiquelhappy end
        } catch (Exception e) {
            RandomSpawnPlus.getInstance().getLogger().warning("The spawn finder failed to find a valid spawn, and has not given " + player.getUniqueId() + " a random spawn. If you find this happening a lot, then raise the 'spawn-finder-tries-before-timeout' key in the config.");
            return;
        }

        RSPLoginListener.firstJoinPlayers.remove(player.getUniqueId());
    }
}
