package og.spigot.survival.spawnplugin.listener;

import og.spigot.survival.spawnplugin.database.DAO;
import og.spigot.survival.spawnplugin.main.OGSpawnPluginMain;
import og.spigot.survival.spawnplugin.utils.OGSpawn;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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
                if(inventoryClickEvent.getView().getTitle().equals("Private Spawns")){
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
                }else if(inventoryClickEvent.getView().getTitle().equals("Private Spawns Configuration")){
                    inventoryClickEvent.setCancelled(true);
                    if(inventoryClickEvent.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE){
                        p.closeInventory();
                        OGSpawn deleted = OGSpawnUtils.getOGSpawnUtils().removePrivateSpawnBySpawnName(p, inventoryClickEvent.getInventory().getItem(13).getItemMeta().getDisplayName());
                        DAO.getDataAccessObject().removePrivateSpawn(deleted);
                        p.sendMessage("§7[§3OGWarpEssentials§7] >> Der Spawnpunkt mit dem Namen §e" + inventoryClickEvent.getInventory().getItem(13).getItemMeta().getDisplayName() + " §7wurde erfolgreich entfernt");
                        OGSpawnUtils.getOGSpawnUtils().removePlayerFromConfigProcess(p);
                    }else if(inventoryClickEvent.getCurrentItem().getType() == Material.OAK_SIGN){
                        p.closeInventory();
                        p.sendMessage("§7[§3OGWarpEssentials§7] >> Bitte gib den neuen Namen für deinen Spawnpunkt ein");
                    }else if(inventoryClickEvent.getCurrentItem().getType() == Material.ARROW){
                        buildCustomConfigInventory(p, OGSpawnUtils.getOGSpawnUtils().getCurrentConfigPage(p));
                    }
                }
            }
        }catch (Exception ex){
            ex.getLocalizedMessage();
        }
    }

    @EventHandler
    public void onPlayerSpawnRenameProcess(AsyncPlayerChatEvent asyncPlayerChatEvent){
        if(OGSpawnUtils.getOGSpawnUtils().isPlayerInConfigMode(asyncPlayerChatEvent.getPlayer())){
            asyncPlayerChatEvent.setCancelled(true);

            OGSpawn spawn =OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(asyncPlayerChatEvent.getPlayer()).get(OGSpawnUtils.getOGSpawnUtils().getCurrentConfigPage(asyncPlayerChatEvent.getPlayer()));
            asyncPlayerChatEvent.getPlayer().sendMessage("§7[§3OGWarpEssentials§7] >> Der Name des Spawnpunktes §e" + spawn.getSpawnName() + " §7wurde erfolgreich auf §e" + asyncPlayerChatEvent.getMessage() + " §7geändert");
            spawn.setSpawnName(asyncPlayerChatEvent.getMessage());
            OGSpawnUtils.getOGSpawnUtils().removePlayerFromConfigProcess(asyncPlayerChatEvent.getPlayer());
        }
    }

    public void buildCustomConfigInventory(Player p, int index){
        Inventory inv = Bukkit.createInventory(p, 45, "Private Warps Configuration");

        int indexSpawnItems = 13;
        int indexChoices = 28;
        int indexPageSwap = 36;

        List<OGSpawn> playerSpawns = OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(p);

        inv.setItem(indexSpawnItems, new ItemStack(playerSpawns.get(index).getSpawnIcon()));
        inv.setItem(indexChoices, new ItemStack(Material.OAK_SIGN));
        indexChoices += 6;
        inv.setItem(indexChoices, new ItemStack(Material.RED_STAINED_GLASS_PANE));

        if(index != 0){
            inv.setItem(indexPageSwap, new ItemStack(Material.ARROW));
        }
        if(playerSpawns.size() != --index){
            indexPageSwap+=8;
            inv.setItem(indexPageSwap, new ItemStack(Material.ARROW));
        }

        p.openInventory(inv);
        OGSpawnUtils.getOGSpawnUtils().addPlayerToConfigProcess(p, ++index);
    }
}
