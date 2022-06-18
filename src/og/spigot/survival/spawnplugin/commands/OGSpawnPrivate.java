package og.spigot.survival.spawnplugin.commands;

import og.spigot.survival.spawnplugin.main.OGSpawnPluginMain;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.Map;

public class OGSpawnPrivate implements CommandExecutor {
    /**
     * This function handles the /ogspawn (<Spawn>) command
     *
     * @param commandSender Represents the player or console sending the command
     * @param command Represents the command object that is created when the command is sent
     * @param s
     * @param strings String array that contains all parameters that are entered
     * @return Returns, if the execution was successful
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(command.getName().equals("ogspawn")){
                Player p = (Player) commandSender;
                Map<String, Location> playerSpawn = OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(p);

                if(!playerSpawn.isEmpty() || OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn() != null){
                    if (strings.length > 0){
                        if(playerSpawn.containsKey(strings[0])){
                            commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> §2Teleportiere...");
                            p.teleport(playerSpawn.get(strings[0]));
                        }else{
                            commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Es konnte kein Spawn mit dem Namen §c" + strings[0] + " §7gefunden werden");
                        }
                    }else{
                        inventoryBuilder(p);
                    }
                }else{
                    commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Du hast noch keine Spawnpunkte gesetzt. Füge neue Spawnpunkte mit § /ogsetspawn <Name> §7hinzu");
                }
            }else{
                commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Falsche Benutzung. Bitte nutze §c/ogspawn <Spawnpunkt>");
            }
            return true;
        }
        return false;
    }

    /**
     * This function is used to build and open the inventory to the player containing all its private spawns
     *
     * @author Schmostrong
     * @param player Represents the player, who receives the inventory
     */
    public void inventoryBuilder(Player player){
        Inventory inv = Bukkit.createInventory(player, 27, "Private Warps");

        if(OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn() != null){
            inv.setItem(4, new ItemStack(Material.BEACON));
            ItemMeta publicSpawnMeta = inv.getItem(4).getItemMeta();
            publicSpawnMeta.setDisplayName("Public Spawn");
            inv.getItem(4).setItemMeta(publicSpawnMeta);
        }

        int index = 10;
        ItemMeta privateSpawnMeta;

        for(Map.Entry<String, Location> playerSpawns : OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(player).entrySet()){
            inv.setItem(index, new ItemStack(Material.ENDER_PEARL));
            privateSpawnMeta = inv.getItem(index).getItemMeta();
            privateSpawnMeta.setDisplayName(playerSpawns.getKey());
            inv.getItem(index).setItemMeta(privateSpawnMeta);
            index++;

            if(index % 8 == 0){
                index+=2;
            }
        }

        if(!inv.isEmpty()){
            player.openInventory(inv);
        }
    }
}
