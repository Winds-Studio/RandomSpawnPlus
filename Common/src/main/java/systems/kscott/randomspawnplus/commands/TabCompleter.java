package systems.kscott.randomspawnplus.commands;

import org.jetbrains.annotations.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    private final List<String> tabCompletion = new ArrayList<>(Arrays.asList(
            "help",
            "reload",
            "wild"
    ));

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            for (String completion : tabCompletion) {
                final String arg = args[0];
                if (completion.startsWith(arg) && sender.hasPermission("rsp.command." + arg)) {
                    completions.add(completion);
                }
            }

            return completions;
        }

        return null;
    }
}
