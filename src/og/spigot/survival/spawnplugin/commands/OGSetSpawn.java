package og.spigot.survival.spawnplugin.commands;

import og.spigot.survival.spawnplugin.main.OGSpawnPluginMain;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OGSetSpawn implements CommandExecutor {
    /**
     * This function handles the /ogsetspawn command
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
            if(command.getName().equals("ogsetspawn")){
                Player p = (Player) commandSender;
                Location spawnLoc = p.getLocation();

                if(strings.length > 0){
                    OGSpawnUtils.getOGSpawnUtils().addPlayerSpawn(p, strings[0], spawnLoc);
                    commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Der Spawnpunkt wurde in deine private Liste mit Namen §e" + strings[0] + " §7aufgenommen");
                    return true;
                }else{
                    OGSpawnUtils.getOGSpawnUtils().setGlobalSpawn(spawnLoc);
                    OGSpawnPluginMain.getMainConfig().addDefault("world", spawnLoc.getWorld());
                    OGSpawnPluginMain.getMainConfig().set("world", spawnLoc.getWorld().getName());
                    OGSpawnPluginMain.getMainConfig().addDefault("X", spawnLoc.getBlockX());
                    OGSpawnPluginMain.getMainConfig().set("X", spawnLoc.getBlockX());
                    OGSpawnPluginMain.getMainConfig().addDefault("Y", spawnLoc.getBlockY());
                    OGSpawnPluginMain.getMainConfig().set("Y", spawnLoc.getBlockY());
                    OGSpawnPluginMain.getMainConfig().addDefault("Z", spawnLoc.getBlockZ());
                    OGSpawnPluginMain.getMainConfig().set("Z", spawnLoc.getBlockZ());
                    commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Der globale Spawnpunkt wurde erfolgreich auf deine aktuelle Position gesetzt");
                    return true;
                }
            }else{
                commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Falsche Nutzung. §e/setspawn §7zum Setzen des Spawnpunktes verwenden");
                return true;
            }
        }
        return false;
    }
}
