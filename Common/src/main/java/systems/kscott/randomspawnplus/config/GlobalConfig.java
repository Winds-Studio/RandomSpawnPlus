package systems.kscott.randomspawnplus.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;
import io.github.thatsmusic99.configurationmaster.api.ConfigSection;
import systems.kscott.randomspawnplus.RandomSpawnPlus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GlobalConfig {

    private static ConfigFile configFile;

    public boolean randomSpawnEnabled = true;
    public String respawnWorld = "world";
    public int spawnCacheCount = 150;
    public int spawnCacheTimeout = 1000;
    public int spawnFindingFailedThreshold = 50;

    public int spawnRangeMinX = -1000;
    public int spawnRangeMaxX = 1000;
    public int spawnRangeMinZ = -1000;
    public int spawnRangeMaxZ = 1000;

    public boolean blockedSpawnZoneEnabled = true;
    public int blockedSpawnZoneMinX = -100;
    public int blockedSpawnZoneMaxX = 100;
    public int blockedSpawnZoneMinZ = -100;
    public int blockedSpawnZoneMaxZ = 100;

    public boolean randomSpawnOnDeath = true;
    public boolean randomSpawnOnFirstJoin = true;
    public boolean randomSpawnAtBed = true;
    public List<String> unsafeBlocks = new ArrayList<>(Arrays.asList(
            "#VINE", "#AIR", "WATER", "LAVA" // TODO Note: Add implementation for #Liquid
    ));
    public boolean randomSpawnUsePermNode = false;

    public String homeIntegrationMode = "essentialsx"; // TODO Note: Use enum
    public boolean useHomeOnDeath = true; // TODO Note: override on-death
    public boolean setHomeOnFirstJoinSpawn = true; // TODO Note: override on-first-join

    public boolean wildEnabled = true;
    public int wildCooldown = 300;
    public int wildDelay = 0;
    public boolean wildAllowFirstUseForAll = false;
    public int wildCost = 100;
    public boolean setHomeOnWild = false;

    public GlobalConfig(File pluginFolder, String configFileName, boolean init) throws Exception {
        configFile = ConfigFile.loadConfig(new File(pluginFolder, configFileName));

        // Get config last version for upgrade task
        Config.getLastConfigVersion(getString("config-version"));

        // Set current config version
        configFile.set("config-version", Config.getCurrentConfigVersion());

        // Add config header
        addCommentRegionBased("config-version",
                Config.getGlobalConfigHeader() +
                        "\n" +
                        "# NOTE: When modifying values such as spawn-range, and the cache is enabled, you'll need to reset spawns.yml.\n" +
                        "# This can be accomplished by simply deleting the spawns.yml and restarting the server.\n" +
                        "\n" +
                        "Don't touch this!\n"
        );

        // Pre-structure to force order
        structureConfig();

        // Init value for config keys
        initConfig();

        // Validate values and disable plugin directly.
        validateConfigValues();
    }

    private void initConfig() {
        final String generalPath = "general.";
        final String spawnControlPath = "spawn-control.";
        final String hooksPath = "hooks.";
        final String wildCommandPath = "wild-command.";

        randomSpawnEnabled = getBoolean(generalPath + "random-spawn-enabled", randomSpawnEnabled, "Enable the random spawning feature? (disable this if you just want /wild)");
        respawnWorld = getString(generalPath + "respawn-world", respawnWorld, "Which world to respawn players in?");
        spawnCacheCount = getInt(generalPath + "spawn-cache-count", spawnCacheCount, "How many spawn locations should RandomSpawnPlus aim to keep cached?");
        spawnCacheTimeout = getInt(generalPath + "spawn-cache-timeout", spawnCacheTimeout, "How long the spawn locations cache should be refreshed?");
        spawnFindingFailedThreshold = getInt(generalPath + "spawn-finding-failed-threshold", spawnFindingFailedThreshold, "How many tries to find a valid spawn before the plugin times out?");

        final String spawnRangePath = generalPath + "spawn-range.";
        addComment(generalPath + "spawn-range", "The spawn range");
        spawnRangeMinX = getInt(spawnRangePath + "min-x", spawnRangeMinX);
        spawnRangeMaxX = getInt(spawnRangePath + "max-x", spawnRangeMaxX);
        spawnRangeMinZ = getInt(spawnRangePath + "min-z", spawnRangeMinZ);
        spawnRangeMaxZ = getInt(spawnRangePath + "max-z", spawnRangeMaxZ);

        final String blockedSpawnZonePath = generalPath + "blocked-spawn-zone.";
        addComment(generalPath + "blocked-spawn-zone", "The blocking range to prevent spawns in");
        blockedSpawnZoneEnabled = getBoolean(blockedSpawnZonePath + "enabled", blockedSpawnZoneEnabled);
        blockedSpawnZoneMinX = getInt(blockedSpawnZonePath + "min-x", blockedSpawnZoneMinX);
        blockedSpawnZoneMaxX = getInt(blockedSpawnZonePath + "max-x", blockedSpawnZoneMaxX);
        blockedSpawnZoneMinZ = getInt(blockedSpawnZonePath + "min-z", blockedSpawnZoneMinZ);
        blockedSpawnZoneMaxZ = getInt(blockedSpawnZonePath + "max-z", blockedSpawnZoneMaxZ);

        randomSpawnOnDeath = getBoolean(spawnControlPath + "on-death", randomSpawnOnDeath, "Will random spawn on player death?");
        randomSpawnOnFirstJoin = getBoolean(spawnControlPath + "on-first-join", randomSpawnOnFirstJoin, "Will random spawn on player first join?");
        randomSpawnAtBed = getBoolean(spawnControlPath + "spawn-at-bed", randomSpawnAtBed, "Will send the player to their bed on death?");
        unsafeBlocks = getList(spawnControlPath + "unsafe-blocks", unsafeBlocks,
                "Blocks that the player shouldn't be able to spawn on\n" +
                        "Please use POST-FLATTENING 1.13+ block IDs - https://jd.papermc.io/paper/1.20/org/bukkit/Material.html\n"
        );
        randomSpawnUsePermNode = getBoolean(spawnControlPath + "use-permission-node", randomSpawnUsePermNode, "Will only random spawn for player who has `randomspawnplus.randomspawn` permission node?");

        homeIntegrationMode = getString(hooksPath + "home-integration-mode", homeIntegrationMode);
        useHomeOnDeath = getBoolean(hooksPath + "use-home-on-death", useHomeOnDeath, "Use plugin's home on random spawn when dead");
        setHomeOnFirstJoinSpawn = getBoolean(hooksPath + "set-home-first-join-random-spawn", setHomeOnFirstJoinSpawn, "Use plugin's home on first join random spawn");

        wildEnabled = getBoolean(wildCommandPath + "wild-enabled", wildEnabled, "Note: changes to this variable will require a restart.");
        wildCooldown = getInt(wildCommandPath + "wild-cooldown", wildCooldown,
                "Unit: seconds, how long between /wild uses?\n" +
                        "Set to 0 to disable.\n" +
                        "Set to -1 to blow up everything."
        );
        wildDelay = getInt(wildCommandPath + "wild-delay", wildDelay);
        wildAllowFirstUseForAll = getBoolean(wildCommandPath + "wild-allow-first-use-no-permission", wildAllowFirstUseForAll,
                "When /wild is ran, should RandomSpawnPlus remove the `randomspawnplus.wild` permission from the executor?\n" +
                        "NOTE: Requires LuckPerms to be installed to manage permissions."
        );
        wildCost = getInt(wildCommandPath + "wild-cost", wildCost,
                "How much should it cost for a user to use /wild?\n" +
                        "Set to 0 to disable this feature.\n" +
                        "NOTE: Requires Vault & and a Vault-compatible econ plugin to function!"
        );
        setHomeOnWild = getBoolean(wildCommandPath + "set-home-on-wild", setHomeOnWild, "Will set an Essentials home if no home is set on /wild");
    }

    private void validateConfigValues() {
        List<String> errors = new ArrayList<>();

        if (RandomSpawnPlus.getInstance().getServer().getWorld(respawnWorld) == null) {
            errors.add("");
        }

        // Collect, print then throw error
        for (String error : errors) {
            RandomSpawnPlus.LOGGER.error(error);
        }

        //throw new RuntimeException();
    }

    public void saveConfig() throws Exception {
        configFile.save();
    }

    private void structureConfig() {
        createTitledSection("General", "general");
        createTitledSection("Random Spawn Control", "spawn-control");
        createTitledSection("Hooks", "hooks");
        createTitledSection("/wild", "wild-command");
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
