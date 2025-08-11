package systems.kscott.randomspawnplus.commands.subcommands;

import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.commands.RSPCommand;
import systems.kscott.randomspawnplus.config.Config;
import org.bukkit.command.CommandSender;

public class CommandReload extends RSPCommand {

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Config.reloadConfig(RandomSpawnPlus.getInstance(), sender);
    }
}
