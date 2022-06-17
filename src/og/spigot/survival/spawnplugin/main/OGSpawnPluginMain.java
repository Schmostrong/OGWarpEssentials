package og.spigot.survival.spawnplugin.main;

import og.spigot.survival.spawnplugin.commands.OGSetSpawn;
import og.spigot.survival.spawnplugin.commands.OGSpawnPrivate;
import og.spigot.survival.spawnplugin.commands.Spawn;
import og.spigot.survival.spawnplugin.listener.OGSpawnPluginListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class OGSpawnPluginMain extends JavaPlugin {
    private static FileConfiguration mainConfiguration;

    @Override
    public void onEnable() {
        mainConfiguration = this.getConfig();

        getCommand("ogsetspawn").setExecutor(new OGSetSpawn());
        getCommand("ogspawn").setExecutor(new OGSpawnPrivate());
        getCommand("spawn").setExecutor(new Spawn());
        getServer().getPluginManager().registerEvents(new OGSpawnPluginListener(), this);

        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdirs();
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.saveDefaultConfig();
        this.saveConfig();
    }

    public static FileConfiguration getMainConfig(){
        return mainConfiguration;
    }
}
