package systems.kscott.randomspawnplus.config;

import io.github.thatsmusic99.configurationmaster.api.ConfigFile;

import java.io.File;

public class SpawnStorage {

    private static ConfigFile configFile;

    public SpawnStorage(File pluginFolder, String configFileName, boolean init) throws Exception {
        configFile = ConfigFile.loadConfig(new File(pluginFolder, configFileName));

        // Init value for config keys
        initConfig();
    }

    private void initConfig() {
    }

    public void saveConfig() throws Exception {
        configFile.save();
    }
}
