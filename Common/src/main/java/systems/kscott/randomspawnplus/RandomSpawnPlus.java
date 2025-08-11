package systems.kscott.randomspawnplus;

import com.tcoded.folialib.FoliaLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import systems.kscott.randomspawnplus.commands.CommandManager;
import systems.kscott.randomspawnplus.commands.TabCompleter;
import systems.kscott.randomspawnplus.config.Config;
import systems.kscott.randomspawnplus.hooks.HookInstance;
import systems.kscott.randomspawnplus.listeners.OnDeath;
import systems.kscott.randomspawnplus.listeners.OnFirstJoin;
import systems.kscott.randomspawnplus.listeners.OnPreLogin;
import systems.kscott.randomspawnplus.spawn.SpawnGenerator;
import systems.kscott.randomspawnplus.util.PlatformUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class RandomSpawnPlus extends JavaPlugin {

    public static final Logger LOGGER = LogManager.getLogger(RandomSpawnPlus.class.getSimpleName());

    private static RandomSpawnPlus INSTANCE;
    private static HookInstance hookInstance;

    private BukkitAudiences adventure;
    public FoliaLib foliaLib = new FoliaLib(this);

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }

        return this.adventure;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.adventure = BukkitAudiences.create(this);

        Config.loadConfig(INSTANCE);

        registerEvents();
        registerCommands();
        registerHooks();

        PlatformUtil.init();
        SpawnGenerator.init();
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        try {
            Config.getSpawnStorage().saveConfig();
        } catch (IOException e) {
            LOGGER.error("Failed to save " + Config.SPAWN_STORAGE_FILE_NAME + "!", e);
        }
    }

    private void registerEvents() {
        if (!Config.getGlobalConfig().randomSpawnEnabled) return;

        getServer().getPluginManager().registerEvents(new OnDeath(), this);
        getServer().getPluginManager().registerEvents(new OnPreLogin(), this);
        getServer().getPluginManager().registerEvents(new OnFirstJoin(), this);
    }

    private void registerCommands() {
        CommandManager manager = new CommandManager();
        manager.initSubCommands();
        getCommand("rsp").setExecutor(manager);
        getCommand("rsp").setTabCompleter(new TabCompleter());
    }

    public static RandomSpawnPlus getInstance() {
        return INSTANCE;
    }

    private void registerHooks() {
        hookInstance = new HookInstance(this);
    }

    public static HookInstance getHooks() {
        return hookInstance.getInstance();
    }
}
