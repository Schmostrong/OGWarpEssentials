package og.spigot.survival.spawnplugin.listener;

import og.spigot.survival.spawnplugin.database.DAO;
import og.spigot.survival.spawnplugin.main.OGSpawnPluginMain;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class OGSpawnPluginListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
        World w = playerJoinEvent.getPlayer().getWorld();

        int x = (int)OGSpawnPluginMain.getMainConfig().get("X");
        int y = (int)OGSpawnPluginMain.getMainConfig().get("Y");
        int z = (int)OGSpawnPluginMain.getMainConfig().get("Z");

        Location globalSpawn = new Location(w, x, y, z);
        OGSpawnUtils.getOGSpawnUtils().setGlobalSpawn(globalSpawn);
        DAO.getDataAccessObject().retrieveGlobalSpawn(w);
        DAO.getDataAccessObject().retrievePlayerSpawns(playerJoinEvent.getPlayer(), w);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent){
        DAO.getDataAccessObject().savePrivateSpawn(playerQuitEvent.getPlayer());
    }

    @EventHandler
    public void onWarpInventoryDrag(InventoryDragEvent inventoryDragEvent){
        inventoryDragEvent.setCancelled(true);
    }

    @EventHandler
    public void onWarpInventoryClick(InventoryClickEvent inventoryClickEvent){
        Player p =  (Player)inventoryClickEvent.getWhoClicked();
        if(OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(p).containsKey(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName())){
            p.teleport(OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(p).get(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()));
        }
    }
}
