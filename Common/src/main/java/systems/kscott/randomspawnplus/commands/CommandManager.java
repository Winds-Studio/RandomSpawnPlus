package systems.kscott.randomspawnplus.commands;

import org.jetbrains.annotations.NotNull;
import systems.kscott.randomspawnplus.commands.subcommands.CommandHelp;
import systems.kscott.randomspawnplus.commands.subcommands.CommandReload;
import systems.kscott.randomspawnplus.commands.subcommands.CommandWild;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private List<RSPCommand> commands;

    public void initSubCommands() {
        commands = Arrays.asList(
                new CommandHelp(),
                new CommandReload(),
                new CommandWild()
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmdLabel, String[] args) {
        String label;

        // Redirect to help command if no sub label
        if (args.length == 0) {
            label = "help";
        } else {
            label = args[0];
        }

        RSPCommand cmd = get(label);

        // Unknown command, redirect to help command
        if (cmd == null) {
            cmd = get("help");
        }

        String[] trimmedArgs = Arrays.copyOfRange(args, 1, args.length);
        cmd.onCommand(sender, trimmedArgs);

        return false;
    }

    private RSPCommand get(String name) {
        for (RSPCommand cmd : commands) {
            if (cmd.command().equalsIgnoreCase(name))
                return cmd;
        }
        return null;
    }
}
