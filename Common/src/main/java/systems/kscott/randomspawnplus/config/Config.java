package systems.kscott.randomspawnplus.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import systems.kscott.randomspawnplus.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Config {

    private static final String CURRENT_REGION = Locale.getDefault().getCountry().toUpperCase(Locale.ROOT);
    private static final String GLOBAL_CONFIG_FILE_NAME = "config.yml";
    private static final String LANG_CONFIG_FILE_NAME = "lang.yml";
    public static final String SPAWN_STORAGE_FILE_NAME = "spawns.yml"; // public since used in other classes

    private static ConfigLocale configLocale;
    private static GlobalConfig globalConfig;
    private static LangConfig langConfig;
    private static SpawnStorage spawnStorage;

    private static final String currConfigVer = "3.0";
    private static int lastConfigVerMajor;
    private static int lastConfigVerMinor;
    private static final int currConfigVerMajor = Integer.parseInt(currConfigVer.split("\\.")[0]);
    private static final int currConfigVerMinor = Integer.parseInt(currConfigVer.split("\\.")[1]);

    @Contract(" -> new")
    public static @NotNull CompletableFuture<Void> reloadAsync(CommandSender sender) {
        return CompletableFuture.runAsync(() -> {
            long begin = System.nanoTime();

            File pluginFolder = RandomSpawnPlus.getInstance().getDataFolder();

            try {
                loadGlobalConfig(pluginFolder, false);
            } catch (Exception e) {
                MessageUtil.broadcastCommandMessage(sender, Component.text("Failed to reload " + GLOBAL_CONFIG_FILE_NAME + ". See error in console!", NamedTextColor.RED));
                RandomSpawnPlus.LOGGER.error(e);
            }
            try {
                loadLangConfig(pluginFolder, false);
            } catch (Exception e) {
                MessageUtil.broadcastCommandMessage(sender, Component.text("Failed to reload " + LANG_CONFIG_FILE_NAME + ". See error in console!", NamedTextColor.RED));
                RandomSpawnPlus.LOGGER.error(e);
            }

            final String success = String.format("Successfully reloaded config in %sms.", (System.nanoTime() - begin) / 1_000_000);
            MessageUtil.broadcastCommandMessage(sender, Component.text(success, NamedTextColor.GREEN));
        });
    }

    public static void loadConfig(Plugin instance) {
        long begin = System.nanoTime();
        RandomSpawnPlus.LOGGER.info("Loading config...");

        final File pluginFolder = instance.getDataFolder();

        try {
            createDirectory(pluginFolder);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to create <{}> plugin folder!", pluginFolder.getAbsolutePath(), e);
        }
        try {
            loadGlobalConfig(pluginFolder, true);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to load " + GLOBAL_CONFIG_FILE_NAME + "!", e);
        }
        try {
            loadLangConfig(pluginFolder, true);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to load " + LANG_CONFIG_FILE_NAME + "!", e);
        }
        try {
            loadSpawnStorage(pluginFolder, true);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to load " + SPAWN_STORAGE_FILE_NAME + "!", e);
        }

        RandomSpawnPlus.LOGGER.info("Successfully loaded config in {}ms.", (System.nanoTime() - begin) / 1_000_000);
    }

    public static void reloadConfig(Plugin instance, CommandSender sender) {
        long begin = System.nanoTime();
        RandomSpawnPlus.LOGGER.info("Reloading config...");

        final File pluginFolder = instance.getDataFolder();

        try {
            createDirectory(pluginFolder);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to create <{}> plugin folder!", pluginFolder.getAbsolutePath(), e);
        }
        try {
            loadGlobalConfig(pluginFolder, false);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to reload " + GLOBAL_CONFIG_FILE_NAME + "!", e);
        }
        try {
            loadLangConfig(pluginFolder, false);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to reload " + LANG_CONFIG_FILE_NAME + "!", e);
        }
        try {
            loadSpawnStorage(pluginFolder, false);
        } catch (Exception e) {
            RandomSpawnPlus.LOGGER.error("Failed to reload " + SPAWN_STORAGE_FILE_NAME + "!", e);
        }

        MessageUtil.send(sender, "&8[&3RandomSpawnPlus&8] Successfully reloaded &bconfig.yml&7, &blang.yml&7, and &bspawns.yml&7 in " + (System.nanoTime() - begin) / 1_000_000 + "ms.");
    }

    private static void loadGlobalConfig(File pluginFolder, boolean init) throws Exception {
        globalConfig = new GlobalConfig(pluginFolder, GLOBAL_CONFIG_FILE_NAME, init);

        globalConfig.saveConfig();
    }

    private static void loadLangConfig(File pluginFolder, boolean init) throws Exception {
        langConfig = new LangConfig(pluginFolder, LANG_CONFIG_FILE_NAME, init);

        langConfig.saveConfig();
    }

    private static void loadSpawnStorage(File pluginFolder, boolean init) throws Exception {
        spawnStorage = new SpawnStorage(pluginFolder, SPAWN_STORAGE_FILE_NAME, init);

        spawnStorage.saveConfig();
    }

    public static void createDirectory(File dir) throws IOException {
        try {
            Files.createDirectories(dir.toPath());
        } catch (FileAlreadyExistsException e) { // Thrown if dir exists but is not a directory
            if (dir.delete()) createDirectory(dir);
        }
    }

    public static String getGlobalConfigHeader() {
        return "#############################\n" +
                "#      RandomSpawnPlus5     #\n" +
                "#       Version 6.0.0       #\n" +
                "#   by @89apt89 & @Dreeam   #\n" +
                "#############################\n";
    }

    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public static LangConfig getLangConfig() {
        return langConfig;
    }

    public static SpawnStorage getSpawnStorage() {
        return spawnStorage;
    }

    public static void getLastConfigVersion(String lastConfigVer) {
        // If last is null, then it means the plugin is fist loaded, fallback to current config version
        if (lastConfigVer == null) lastConfigVer = currConfigVer;

        lastConfigVerMajor = Integer.parseInt(lastConfigVer.split("\\.")[0]);
        lastConfigVerMinor = Integer.parseInt(lastConfigVer.split("\\.")[1]);
    }

    public static String getCurrentConfigVersion() {
        return currConfigVer;
    }

    private ConfigLocale getConfigLocale() {
        if (configLocale != null) {
            return configLocale;
        }

        ConfigLocale locale;
        switch (CURRENT_REGION) {
            case "EN": {
                locale = ConfigLocale.ENGLISH;
                break;
            }
            case "CN": {
                locale = ConfigLocale.CHINESE;
                break;
            }
            case "RU": {
                locale = ConfigLocale.RUSSIAN;
                break;
            }
            default: {
                locale = ConfigLocale.ENGLISH;
                break;
            }
        }

        configLocale = locale;
        return locale;
    }

    enum ConfigLocale {
        ENGLISH,
        CHINESE,
        RUSSIAN
    }
}
