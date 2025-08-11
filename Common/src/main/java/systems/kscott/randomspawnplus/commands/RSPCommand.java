package systems.kscott.randomspawnplus.commands;

import org.bukkit.command.CommandSender;

public abstract class RSPCommand {

    public abstract String command();

    public abstract void onCommand(CommandSender sender, String[] args);
}
