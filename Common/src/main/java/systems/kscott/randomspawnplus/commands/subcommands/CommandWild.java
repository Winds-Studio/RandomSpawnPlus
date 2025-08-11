package systems.kscott.randomspawnplus.commands.subcommands;

import com.earth2me.essentials.User;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.commands.RSPCommand;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.events.RandomSpawnEvent;
import systems.kscott.randomspawnplus.events.SpawnType;
import systems.kscott.randomspawnplus.spawn.SpawnFinder;
import systems.kscott.randomspawnplus.util.MessageUtil;
import systems.kscott.randomspawnplus.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

public class CommandWild extends RSPCommand {

    @Override
    public String command() {
        return "wild";
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return;
        }
    }

    private static void doWild(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            MessageUtil.send(sender, Config.getLangConfig().playerOnly);
            return;
        }

        long cooldown = Util.getCooldown(player);

        if (player.hasPermission("randomspawnplus.wild.bypasscooldown")) {
            cooldown = 0;
        }

        if ((cooldown - Instant.now().toEpochMilli()) >= 0) {
            String message = Config.getLangConfig().wildTpCooldown;
            message = message.replace("%delay%", MessageUtil.getStringFromSeconds(cooldown / 1000 - Instant.now().getEpochSecond()));

            MessageUtil.send(player, message);
            return;
        }

        int wildCost = Config.getGlobalConfig().wildCost;

        if (wildCost != 0 && RandomSpawnPlus.getHooks().getEconomy() != null && !player.hasPermission("randomspawnplus.wild.bypasscost")) {
            if (RandomSpawnPlus.getHooks().getEconomy().has(player, wildCost)) {
                RandomSpawnPlus.getHooks().getEconomy().withdrawPlayer(player, wildCost);
            } else {
                MessageUtil.send(player, Config.getLangConfig().wildNoMoney);
                return;
            }
        }

        Location location;

        try {
            location = SpawnFinder.getRandomSpawn();
        } catch (Exception e) {
            // TODO: Refactor here, no need to use try/catch to handle failed finding spawn
            MessageUtil.send(player, Config.getLangConfig().errorOnFindingSpawn);
            return;
        }

        String message = Config.getLangConfig().wildTp
                .replace("%x%", Integer.toString(location.getBlockX()))
                .replace("%y%", Integer.toString(location.getBlockY()))
                .replace("%z%", Integer.toString(location.getBlockZ()));

        MessageUtil.send(player, message);

        // TODO: Add essx and other plugin support, like HuskHomes
        /*
        if (Config.getGlobalConfig().setHomeOnWild && RandomSpawnPlus.getHooks().getEssentials() != null) {
            User user = RandomSpawnPlus.getHooks().getEssentials().getUser(player);
            if (!user.hasHome()) {
                user.setHome("home", location);
                user.save();
            }
        }
         */

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, player, SpawnType.WILD_COMMAND);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);
        RandomSpawnPlus.getInstance().foliaLib.getScheduler().teleportAsync(player, location.add(0.5, 0, 0.5));
        Util.addCooldown(player);
    }

    private static void doWildOther(CommandSender sender, String playerName) {
        Player otherPlayer = Bukkit.getPlayer(playerName);

        if (otherPlayer == null) {
            MessageUtil.send(sender, Config.getLangConfig().invalidPlayer);
            return;
        }

        Location location;

        try {
            location = SpawnFinder.getRandomSpawn();
        } catch (Exception e) {
            // TODO: Refactor here, no need to use try/catch to handle failed finding spawn
            MessageUtil.send(otherPlayer, Config.getLangConfig().errorOnFindingSpawn);
            return;
        }

        String message = Config.getLangConfig().wildTp
                .replace("%x%", Integer.toString(location.getBlockX()))
                .replace("%y%", Integer.toString(location.getBlockY()))
                .replace("%z%", Integer.toString(location.getBlockZ()));

        MessageUtil.send(otherPlayer, message);

        message = Config.getLangConfig().wildTpOther.replace("%player%", otherPlayer.getName());

        MessageUtil.send(sender, message);

        RandomSpawnEvent randomSpawnEvent = new RandomSpawnEvent(location, otherPlayer.getPlayer(), SpawnType.WILD_COMMAND);

        Bukkit.getServer().getPluginManager().callEvent(randomSpawnEvent);

        if (!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }

        RandomSpawnPlus.getInstance().foliaLib.getScheduler().teleportAsync(otherPlayer, location.add(0.5, 0, 0.5));
    }
}
