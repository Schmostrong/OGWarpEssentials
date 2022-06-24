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
import org.bukkit.inventory.meta.ItemMeta;

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
                    }else{
                        commandSender.sendMessage("§7[§3OGWarpEssentials§7] >> Du hast noch keine privaten Spawns gesetzt und kannst daher nichts bearbeiten");
                    }
                }else{
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

        if(playerSpawns.get(previousPage).getSpawnIcon() == Material.AIR || playerSpawns.get(previousPage).getSpawnIcon() == Material.LEGACY_AIR || playerSpawns.get(previousPage).getSpawnIcon() == null){
            inv.setItem(indexSpawnItems, new ItemStack(Material.ENDER_PEARL));
            ItemMeta itemMetaBeta = inv.getItem(indexSpawnItems).getItemMeta();
            itemMetaBeta.setDisplayName(playerSpawns.get(previousPage).getSpawnName());
            inv.getItem(indexSpawnItems).setItemMeta(itemMetaBeta);
        }else{
            inv.setItem(indexSpawnItems, new ItemStack(playerSpawns.get(previousPage).getSpawnIcon()));
            ItemMeta itemMetaBeta = inv.getItem(indexSpawnItems).getItemMeta();
            itemMetaBeta.setDisplayName(playerSpawns.get(previousPage).getSpawnName());
            inv.getItem(indexSpawnItems).setItemMeta(itemMetaBeta);
        }
        inv.setItem(indexChoices, new ItemStack(Material.ANVIL));
        ItemMeta itemMeta = inv.getItem(indexChoices).getItemMeta();
        itemMeta.setDisplayName("Rename Private Spawn");
        inv.getItem(indexChoices).setItemMeta(itemMeta);
        indexChoices += 6;
        inv.setItem(indexChoices, new ItemStack(Material.TNT));
        itemMeta = inv.getItem(indexChoices).getItemMeta();
        itemMeta.setDisplayName("Delete Private Spawn");
        inv.getItem(indexChoices).setItemMeta(itemMeta);
        indexPageSwap+=8;

        if(playerSpawns.size() > 1){
            inv.setItem(indexPageSwap, new ItemStack(Material.ARROW));
            itemMeta = inv.getItem(indexPageSwap).getItemMeta();
            itemMeta.setDisplayName("Next Private Spawn");
            inv.getItem(indexPageSwap).setItemMeta(itemMeta);
        }

        p.openInventory(inv);
        OGSpawnUtils.getOGSpawnUtils().addPlayerToConfigProcess(p, previousPage);
    }
}
