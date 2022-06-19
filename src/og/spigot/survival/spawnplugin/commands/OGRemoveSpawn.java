package og.spigot.survival.spawnplugin.commands;

import og.spigot.survival.spawnplugin.database.DAO;
import og.spigot.survival.spawnplugin.utils.OGSpawn;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OGRemoveSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(command.getName().equals("ogremovespawn")){
                if(strings.length > 0){
                    Player p = (Player) commandSender;

                    for(OGSpawn spawn : OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(p)){
                        if(spawn.getSpawnName().equals(strings[0])){
                            OGSpawnUtils.getOGSpawnUtils().removePrivateSpawn(spawn);
                            DAO.getDataAccessObject().removePrivateSpawn(spawn);
                            commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Der Spawnpunkt mit dem Namen §e" + strings[0] + " §7wurde erfolgreich entfernt");
                            return true;
                        }
                    }

                    commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Es konnte kein Spawnpunkt mit dem Namen §c" + strings[0] + " §7gefunden werden");
                }else{
                    commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Bitte gib an, welchen SpawnPunkt du entfernen möchtest");
                }
            }else{
                commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Falsche Nutzung. §e/ogremovespawn §7 zum Setzen des Spawnpunktes verwenden");
            }
            return true;
        }
        return false;
    }
}
