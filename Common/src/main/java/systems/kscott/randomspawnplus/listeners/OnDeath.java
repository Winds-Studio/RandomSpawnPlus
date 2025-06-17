package systems.kscott.randomspawnplus.listeners;

import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerRespawnEvent event) {
        if (!Config.getGlobalConfig().randomSpawnOnDeath) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isDead()) return;

        if (Config.getGlobalConfig().randomSpawnUsePermNode && !player.hasPermission("randomspawnplus.randomspawn"))
            return;

        if (Config.getGlobalConfig().randomSpawnAtBed && player.getBedSpawnLocation() != null) {
            event.setRespawnLocation(player.getBedSpawnLocation());
            return;
        }

        Location location;

        try {
            location = SpawnFinder.getRandomSpawn();
        } catch (Exception e) {
            RandomSpawnPlus.getInstance().getLogger().warning("The spawn finder failed to find a valid spawn, and has not given " + player.getName() + " a random spawn. If you find this happening a lot, then raise the 'spawn-finder-tries-before-timeout' key in the config.");
            return;
        }

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, player, SpawnType.ON_DEATH);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
        event.setRespawnLocation(location);
    }
}
