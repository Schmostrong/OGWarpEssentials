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

/**
 * The class represents all listeners for occuring events and the plugins reaction to them
 */
public class OGSpawnPluginListener implements Listener {
    /**
     * On playerJoin it's private spawns are loaded into the runtime data and the global spawn is set to its location
     * @param playerJoinEvent Represents the event occuring when a player joins
     */
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

    /**
     * On players leave, it's private spawns are saved into the database and unloaded from the runtime data
     * @param playerQuitEvent Represents the event occuring when a player quits the server
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent){
        DAO.getDataAccessObject().savePrivateSpawn(playerQuitEvent.getPlayer());
        OGSpawnUtils.getOGSpawnUtils().unloadPlayersData(playerQuitEvent.getPlayer());
    }

    /**
     * The function is used when the private spawns GUI is opened and a player tries to drag items. The event is cancelled.
     * @param inventoryDragEvent Represents the event occuring when a player tries to drag items in an inventory
     */
    @EventHandler
    public void onWarpInventoryDrag(InventoryDragEvent inventoryDragEvent){
        inventoryDragEvent.setCancelled(true);
    }

    /**
     * The function is used when the private spawns GUI is opened and a player clicks on a spawn location
     * @param inventoryClickEvent Represents the event occuring when a player clicks an item in an inventory
     */
    @EventHandler
    public void onWarpInventoryClick(InventoryClickEvent inventoryClickEvent){
        Player p =  (Player)inventoryClickEvent.getWhoClicked();
        if(OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(p).containsKey(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName())){
            p.teleport(OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(p).get(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName()));
        }else if(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().equals("Public Spawn")){
            p.teleport(OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn());
        }
    }
}
