package systems.kscott.randomspawnplus.hooks;

import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.popcraft.chunky.api.ChunkyAPI;
import systems.kscott.randomspawnplus.RandomSpawnPlus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class HookInstance {

    private final RandomSpawnPlus instance;

    private IEssentials essentials;
    private LuckPerms luckPerms;
    private Economy economy;
    private ChunkyAPI chunky;

    public HookInstance(RandomSpawnPlus pluginInstance) {
        instance = pluginInstance;

        registerHooks();
    }

    private void registerHooks() {
        new Metrics(instance, 6465); // TODO Note: Use own bstats, since no one update.

        Plugin essPlugin = instance.getServer().getPluginManager().getPlugin("Essentials");

        if (essPlugin != null) {
            essentials = (IEssentials) essPlugin;
        }

        if (instance.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            setupPermissions();
        } else {
            RandomSpawnPlus.LOGGER.warn("The LuckPerms API is not detected, so the 'remove-permission-on-first-use' config option will not be enabled.");
        }

        if (instance.getServer().getPluginManager().getPlugin("Vault") != null) {
            setupEconomy();
        } else {
            RandomSpawnPlus.LOGGER.warn("The Vault API is not detected, so /wild cost will not be enabled.");
        }

        if (instance.getServer().getPluginManager().getPlugin("Chunky") != null) {
            chunky = Bukkit.getServer().getServicesManager().load(ChunkyAPI.class);
        } else {
            RandomSpawnPlus.LOGGER.warn("You need to have Chunky installed to use RandomSpawnPlus.");
            RandomSpawnPlus.getInstance().getPluginLoader().disablePlugin(instance);
        }
    }

    private void setupPermissions() {
        RegisteredServiceProvider<LuckPerms> rsp = instance.getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp != null) {
            luckPerms = rsp.getProvider();
        }
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }
    }

    public HookInstance getInstance() {
        return this;
    }

    public IEssentials getEssentials() {
        return essentials;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public Economy getEconomy() {
        return economy;
    }

    public ChunkyAPI getChunky() {
        return chunky;
    }
}
