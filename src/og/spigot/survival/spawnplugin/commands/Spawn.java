package og.spigot.survival.spawnplugin.commands;

import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player){
            if(command.getName().equals("spawn")){

                Player p = (Player) commandSender;
                try{
                    if(OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn() == null){
                        commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Es wurde noch kein Spawnpunkt gesetzt. Mit §e /spawn §7 kann der Spawn gesetzt werden");
                        return true;
                    }else{
                        commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> §2Teleportiere...");
                        p.teleport(OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn());
                        return true;
                    }
                }catch (NullPointerException ex){
                    commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Es wurde noch kein Spawnpunkt gesetzt. Mit §e /spawn §7 kann der Spawn gesetzt werden");
                    return true;
                }
            }else{
                commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Falsche Nutzung. Nutze §e/spawn §7um dich zum Spawn zu teleportieren");
                return true;
            }
        }
        return false;
    }
}
