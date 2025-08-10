package systems.kscott.randomspawnplus.config;

import systems.kscott.randomspawnplus.RandomSpawnPlus;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SpawnStorage {

    private FileConfiguration config;
    private final String configFileName;

    public SpawnStorage(File pluginFolder, String configFileName, boolean init) throws Exception {
        this.configFileName = configFileName;

        // Init value for config keys
        reloadConfig();
    }

    public FileConfiguration get() {
        return config;
    }

    public void reloadConfig() throws IOException, InvalidConfigurationException {
        File customConfigFile = createFile();
        config = new YamlConfiguration();

        config.load(customConfigFile);
    }

    public void saveConfig(File pluginFolder) throws IOException {
        // TODO: Check here, better way?
        String path = Paths.get(pluginFolder.getAbsolutePath(), configFileName).toString();
        config.save(path);
    }

    private File createFile() {
        File customConfigFile = new File(RandomSpawnPlus.getInstance().getDataFolder(), configFileName);
        if (!customConfigFile.exists() || customConfigFile.getParentFile().mkdirs()) {
            RandomSpawnPlus.getInstance().saveResource(configFileName, false);
        }
        return customConfigFile;
    }
}
