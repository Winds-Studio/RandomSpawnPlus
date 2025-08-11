package systems.kscott.randomspawnplus.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.io.File;
import java.util.List;
import java.util.Map;

public class LangConfig {

    private static ConfigFile configFile;

    public String noPerm = "&c[!] &7You don't have permission to execute this command.";
    public String playerOnly = "&c[!] &7You must be a player to execute this command.";
    public String errorOnFindingSpawn = "&cUnfortunately, I could not find a valid spawn for you. Please try again.";
    public String noSpawnFound = "The spawn cacher is enabled, but there are no spawns cached. RSP will fall back to on-the-fly spawn generation. This is most likely an issue with your configuration, but you can join the support Discord (or contact apt#8099 on Discord) for help.";
    public String invalidPlayer = "&c[!] &7That player does not exist!";
    public String spawnNotInitialized = "&c[!] &7Spawn is still initializing, try to join later!";

    public String wildTp = "&a&l[!] &7You've been teleported to &b%x&7, &b%y&7, &b%z&7.";
    public String wildTpOther = "&a&l[!] &b%player&7 has been successfully teleported.";
    public String wildTpNonExist = "&c&l[!] &7That player doesn't exist.";
    public String wildTpCooldown = "&c&l[!] &7You must wait&c%delay&7 to use that command.";
    public String wildNoMoney = "&c[!] &7You don't have enough money to use &b/wild&7!";

    public String delayDay = "day";
    public String delayDays = "days";
    public String delayHour = "hour";
    public String delayHours = "hours";
    public String delayMin = "minute";
    public String delayMins = "minutes";
    public String delaySec = "second";
    public String delaySecs = "seconds";

    public LangConfig(File pluginFolder, String configFileName, boolean init) throws Exception {
        configFile = ConfigFile.loadConfig(new File(pluginFolder, configFileName));

        // Add config header
        addCommentRegionBased("general",
                Config.getGlobalConfigHeader() +
                        "\n" +
                        "\n"
        );

        // Pre-structure to force order
        structureConfig();

        // Init value for config keys
        initConfig();
    }

    private void initConfig() {
        final String generalPath = "general.";
        final String commandPath = "command.";
        final String delayPath = "delay.";

        noPerm = getString(generalPath + "no-perm", noPerm);
        playerOnly = getString(generalPath + "player-only", playerOnly);
        errorOnFindingSpawn = getString(generalPath + "error-finding-spawn", errorOnFindingSpawn);
        noSpawnFound = getString(generalPath + "no-spawn-found", noSpawnFound); // TODO Note: Should remove?
        invalidPlayer = getString(generalPath + "invalid-player", invalidPlayer);
        spawnNotInitialized = getString(generalPath + "spawn-not-initialized", spawnNotInitialized);

        wildTp = getString(commandPath + "wild-tp", wildTp, "Placeholders: %x% | %y% | %z%");
        wildTpOther = getString(commandPath + "wild-tp-other", wildTpOther, "Placeholders: %player%");
        wildTpNonExist = getString(commandPath + "wild-tp-doesnt-exist", wildTpNonExist);
        wildTpCooldown = getString(commandPath + "wild-tp-cooldown", wildTpCooldown, "Placeholders: %delay%");
        wildNoMoney = getString(commandPath + "wild-no-money", wildNoMoney);

        delayDay = getString(delayPath + "day", delayDay);
        delayDays = getString(delayPath + "days", delayDays);
        delayHour = getString(delayPath + "hour", delayHour);
        delayHours = getString(delayPath + "hours", delayHours);
        delayMin = getString(delayPath + "minute", delayMin);
        delayMins = getString(delayPath + "minutes", delayMins);
        delaySec = getString(delayPath + "second", delaySec);
        delaySecs = getString(delayPath + "seconds", delaySecs);
    }

    public void saveConfig() throws Exception {
        configFile.save();
    }

    private void structureConfig() {
        createTitledSection("General", "general");
        createTitledSection("Command", "command");
        createTitledSection("Delay", "delay");
    }

    // Config Utilities

    /* getAndSet */

    public void createTitledSection(String title, String path) {
        configFile.addSection(title);
        configFile.addDefault(path, null);
    }

    public boolean getBoolean(String path, boolean def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getBoolean(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        configFile.addDefault(path, def);
        return configFile.getBoolean(path, def);
    }

    public String getString(String path, String def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getString(path, def);
    }

    public String getString(String path, String def) {
        configFile.addDefault(path, def);
        return configFile.getString(path, def);
    }

    public double getDouble(String path, double def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getDouble(path, def);
    }

    public double getDouble(String path, double def) {
        configFile.addDefault(path, def);
        return configFile.getDouble(path, def);
    }

    public int getInt(String path, int def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getInteger(path, def);
    }

    public int getInt(String path, int def) {
        configFile.addDefault(path, def);
        return configFile.getInteger(path, def);
    }

    public long getLong(String path, long def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getLong(path, def);
    }

    public long getLong(String path, long def) {
        configFile.addDefault(path, def);
        return configFile.getLong(path, def);
    }

    public List<String> getList(String path, List<String> def, String comment) {
        configFile.addDefault(path, def, comment);
        return configFile.getStringList(path);
    }

    public List<String> getList(String path, List<String> def) {
        configFile.addDefault(path, def);
        return configFile.getStringList(path);
    }

    public ConfigSection getConfigSection(String path, Map<String, Object> defaultKeyValue, String comment) {
        configFile.addDefault(path, null, comment);
        configFile.makeSectionLenient(path);
        defaultKeyValue.forEach((string, object) -> configFile.addExample(path + "." + string, object));
        return configFile.getConfigSection(path);
    }

    public ConfigSection getConfigSection(String path, Map<String, Object> defaultKeyValue) {
        configFile.addDefault(path, null);
        configFile.makeSectionLenient(path);
        defaultKeyValue.forEach((string, object) -> configFile.addExample(path + "." + string, object));
        return configFile.getConfigSection(path);
    }

    /* get */

    public Boolean getBoolean(String path) {
        String value = configFile.getString(path, null);
        return value == null ? null : Boolean.parseBoolean(value);
    }

    public String getString(String path) {
        return configFile.getString(path, null);
    }

    public Double getDouble(String path) {
        String value = configFile.getString(path, null);
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            RandomSpawnPlus.LOGGER.warn("{} is not a valid number, skipped! Please check your configuration.", path, e);
            return null;
        }
    }

    public Integer getInt(String path) {
        String value = configFile.getString(path, null);
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            RandomSpawnPlus.LOGGER.warn("{} is not a valid number, skipped! Please check your configuration.", path, e);
            return null;
        }
    }

    public Long getLong(String path) {
        String value = configFile.getString(path, null);
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            RandomSpawnPlus.LOGGER.warn("{} is not a valid number, skipped! Please check your configuration.", path, e);
            return null;
        }
    }

    public List<String> getList(String path) {
        return configFile.getList(path, null);
    }

    // TODO, check
    public ConfigSection getConfigSection(String path) {
        configFile.addDefault(path, null);
        configFile.makeSectionLenient(path);
        //defaultKeyValue.forEach((string, object) -> configFile.addExample(path + "." + string, object));
        return configFile.getConfigSection(path);
    }

    public void addComment(String path, String comment) {
        configFile.addComment(path, comment);
    }

    public void addCommentRegionBased(String path, String... comments) {
        configFile.addComment(path, comments[0]); // TODO
    }
}
