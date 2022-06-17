package og.spigot.survival.spawnplugin.listener;

import og.spigot.survival.spawnplugin.main.OGSpawnPluginMain;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OGSpawnPluginListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        World w = playerJoinEvent.getPlayer().getWorld();

        int x = (int)OGSpawnPluginMain.getMainConfig().get("X");
        int y = (int)OGSpawnPluginMain.getMainConfig().get("Y");
        int z = (int)OGSpawnPluginMain.getMainConfig().get("Z");

        Location globalSpawn = new Location(w, x, y, z);
        OGSpawnUtils.getOGSpawnUtils().setGlobalSpawn(globalSpawn);
    }
}
