package og.spigot.survival.spawnplugin.commands;

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

import java.util.Locale;
import java.util.Map;

public class OGSpawnPrivate implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(command.getName().equals("ogspawn")){
                Player p = (Player) commandSender;
                Map<String, Location> playerSpawn = OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(p);

                if(!playerSpawn.isEmpty()){
                    if (strings.length > 0){
                        if(playerSpawn.containsKey(strings[0])){
                            commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> §2Teleportiere...");
                            p.teleport(playerSpawn.get(strings[0]));
                        }else{
                            commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Es konnte kein Spawn mit dem Namen §c" + strings[0] + "§7gefunden werden");
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

    public void inventoryBuilder(Player player){
        Inventory inv = Bukkit.createInventory(player, 24, "Private Warps");
        inv.setItem(4, new ItemStack(Material.ENDER_PEARL));

        int index = 10;
        for(Map.Entry<String, Location> playerSpawns : OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(player).entrySet()){
            inv.setItem(index, new ItemStack(Material.ENDER_PEARL));
            inv.getItem(index).getItemMeta().setDisplayName(playerSpawns.getKey());
            index++;

            if(index % 8 == 0){
                index+=2;
            }
        }

        player.openInventory(inv);
    }
}
