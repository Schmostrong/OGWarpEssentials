package og.spigot.survival.spawnplugin.commands;

import og.spigot.survival.spawnplugin.utils.OGSpawn;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OGConfigureSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(command.getName().equals("ogconfigurespawn")){
                Player p = (Player) commandSender;
                if(strings.length == 0){
                    if(!OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(p).isEmpty()){
                        openCustomInventoryWithAllPrivateSpawns(p, 0);
                    }
                }
            }else{
                commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Falsche Nutzung. Mit §e/ogconfigurespawn §7können die privaten Spawns konfiguriert werden");
            }
            return true;
        }
        return false;
    }

    public void openCustomInventoryWithAllPrivateSpawns(Player p, int previousPage){
        Inventory inv = Bukkit.createInventory(p, 45, "Private Warps Configuration");

        int indexSpawnItems = 13;
        int indexChoices = 28;
        int indexPageSwap = 36;

        List<OGSpawn> playerSpawns = OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(p);

        inv.setItem(indexSpawnItems, new ItemStack(playerSpawns.get(previousPage).getSpawnIcon()));
        inv.setItem(indexChoices, new ItemStack(Material.OAK_SIGN));
        indexChoices += 6;
        inv.setItem(indexChoices, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        inv.setItem(indexPageSwap, new ItemStack(Material.ARROW));
        indexPageSwap+=8;
        inv.setItem(indexPageSwap, new ItemStack(Material.ARROW));

        p.openInventory(inv);
        OGSpawnUtils.getOGSpawnUtils().addPlayerToConfigProcess(p, previousPage);
    }
}
