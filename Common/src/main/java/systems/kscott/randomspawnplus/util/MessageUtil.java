package systems.kscott.randomspawnplus.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.config.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void broadcastCommandMessage(CommandSender sender, Object message) {
        PlatformUtil.getPlatform().broadcastCommandMessage(sender, message);
    }

    // move to NMS module
    public static void send(Player player, String... messages) {
        for (String message : messages) {
            RandomSpawnPlus.getInstance().adventure().player(player).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }

    public static void send(CommandSender sender, String... messages) {
        for (String message : messages) {
            RandomSpawnPlus.getInstance().adventure().sender(sender).sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
        }
    }

    public static String getStringFromSeconds(long seconds) {
        final long days = seconds / 86400L;
        final long hours = seconds / 3600L % 24L;
        final long minutes = seconds / 60L % 60L;
        final long secs = seconds % 60L;

        final String daysStr = days > 0L
                ? " " + days + " " + (days != 1 ? Config.getLangConfig().delayDays : Config.getLangConfig().delayDay)
                : "";
        final String hoursStr = hours > 0L
                ? " " + hours + " " + (hours != 1 ? Config.getLangConfig().delayHours : Config.getLangConfig().delayHour)
                : "";
        final String minsStr = minutes > 0L
                ? " " + minutes + " " + (minutes != 1 ? Config.getLangConfig().delayMins : Config.getLangConfig().delayMin)
                : "";
        final String secsStr = secs > 0L
                ? " " + secs + " " + (secs != 1 ? Config.getLangConfig().delaySecs : Config.getLangConfig().delaySec)
                : "";

        return daysStr + hoursStr + minsStr + secsStr;
    }
}
