package systems.kscott.randomspawnplus.commands.subcommands;

import systems.kscott.randomspawnplus.commands.RSPCommand;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.util.MessageUtil;
import org.bukkit.command.CommandSender;

public class CommandReload extends RSPCommand {

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Config.getGlobalConfig().reload();
        Config.getGlobalConfig().reload();
        Config.getGlobalConfig().reload();
        MessageUtil.send(sender, "&8[&3RandomSpawnPlus&8] &7Reloaded &bconfig.yml&7, &blang.yml&7, and &bspawns.yml&7.");
    }
}
