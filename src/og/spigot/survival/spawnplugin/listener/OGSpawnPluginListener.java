package og.spigot.survival.spawnplugin.listener;

import og.spigot.survival.spawnplugin.database.DAO;
import og.spigot.survival.spawnplugin.main.OGSpawnPluginMain;
import og.spigot.survival.spawnplugin.utils.OGSpawn;
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
        DAO.getDataAccessObject().retrieveGlobalSpawn();
        DAO.getDataAccessObject().retrievePlayerSpawns(playerJoinEvent.getPlayer());
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

        try{
            if(inventoryClickEvent.getCurrentItem().getItemMeta() != null){
                for(OGSpawn spawn : OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(p)){
                    if(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().equals(spawn.getSpawnName())){
                        inventoryClickEvent.setCancelled(true);
                        p.teleport(spawn.getSpawnLocation());
                    }
                }
                if(inventoryClickEvent.getCurrentItem().getItemMeta().getDisplayName().equals("Public Spawn")){
                    inventoryClickEvent.setCancelled(true);
                    p.teleport(OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn());
                }
            }
        }catch (Exception ex){
            ex.getLocalizedMessage();
        }
    }
}
