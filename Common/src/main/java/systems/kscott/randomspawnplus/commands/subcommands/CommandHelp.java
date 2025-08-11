package systems.kscott.randomspawnplus.commands.subcommands;

import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.commands.RSPCommand;
import systems.kscott.randomspawnplus.spawn.SpawnData;
import systems.kscott.randomspawnplus.util.MessageUtil;
import systems.kscott.randomspawnplus.util.Permission;
import org.bukkit.command.CommandSender;

public class CommandHelp extends RSPCommand {

    @Override
    public String command() {
        return "help";
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permission.RSP_MANAGE.getPerm())) {
            //sender.sendMessage("No permission"); // TODO: should add this?
            return;
        }

        // TODO: Remove debug
        MessageUtil.send(sender, String.valueOf(SpawnData.isAllSpawnRangeChunksGenerated()));
        MessageUtil.send(sender, String.valueOf(SpawnData.getPendingGenerateChunksList().size()));

        MessageUtil.send(sender,
                "&8[&3RandomSpawnPlus&8] &7Running &bv" + RandomSpawnPlus.getInstance().getDescription().getVersion() + "&7, made with love &a:^)",
                "",
                "&b/rsp &8- &7The help menu.",
                "&b/rsp reload &8- &7Reload the plugin configuration.",
                "&b/wild &8- &7Randomly teleport yourself.",
                "&b/wild <other> &8- &7Randomly teleport another player.",
                "&7Need help? Check out &bhttps://github.com/Winds-Studio/RandomSpawnPlus&7."
        );
    }
}
