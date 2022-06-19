package og.spigot.survival.spawnplugin.main;

import og.spigot.survival.spawnplugin.commands.*;
import og.spigot.survival.spawnplugin.database.DAO;
import og.spigot.survival.spawnplugin.listener.OGSpawnPluginListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class OGSpawnPluginMain extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        if(!this.getDataFolder().exists()){
            this.getDataFolder().mkdirs();
        }

        DAO.getDataAccessObject().createDAOConnection(this.getConfig().getString("mysql_host"),
                                                        this.getConfig().getString("database"),
                                                        this.getConfig().getString("username"),
                                                        this.getConfig().getString("password"),
                                                        this.getConfig().getInt("mysql_port"));
        DAO.getDataAccessObject().prepareTables();

        getCommand("ogsetspawn").setExecutor(new OGSetSpawn());
        getCommand("ogremovespawn").setExecutor(new OGRemoveSpawn());
        getCommand("ogspawn").setExecutor(new OGSpawnPrivate());
        getCommand("ogconfigurespawn").setExecutor(new OGConfigureSpawn());
        getCommand("spawn").setExecutor(new Spawn());
        getServer().getPluginManager().registerEvents(new OGSpawnPluginListener(), this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        DAO.getDataAccessObject().saveAllPrivateSpawns();
        DAO.getDataAccessObject().saveGlobalSpawn();
    }
}
