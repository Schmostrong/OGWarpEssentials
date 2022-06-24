package og.spigot.survival.spawnplugin.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * The class is used to store all data that is used by the plugin during runtime.
 * It gathers the database data at server startup and manages all changes from players immediately
 */
public class OGSpawnUtils {

    /**
     * The variable represents the globalSpawn used for every player
     */
    private Location globalSpawn;

    /**
     * The variable capsulates all private spawns set by the players
     */
    private List<OGSpawn> privateSpawns;

    /**
     * The variable holds the singleton instance of the OGSpawnUtils class
     */
    private static OGSpawnUtils spawnUtils;

    private Map<Player, Integer> currentConfigPage;

    /**
     *
     * Private constructor, so that the singleton design pattern is complete
     */
    private OGSpawnUtils(){
        globalSpawn = null;
        privateSpawns = new LinkedList<>();
        currentConfigPage = new HashMap<>();
    }

    /**
     * The function is used to retrieve the OGSpawnUtils object, since it is implemented using the singletion design pattern
     *
     * @author Schmostrong
     * @return The OGSpawnUtils instance
     */
    public static OGSpawnUtils getOGSpawnUtils(){
        if(spawnUtils != null){
            return spawnUtils;
        }else{
            spawnUtils = new OGSpawnUtils();
            return spawnUtils;
        }
    }

    /**
     * The function is used to set the global spawn for all players
     *
     * @author Schmostrong
     * @param spawnLoc The location where the global spawnPoint shall be set
     */
    public void setGlobalSpawn(Location spawnLoc){
        globalSpawn = spawnLoc;
    }

    /**
     * Function is used to retrieve the global spawn point if any is set
     *
     * @author Schmostrong
     * @return The location of the global spawn point
     */

    public Location getGlobalSpawn(){
        return globalSpawn;
    }

    /**
     * Function is used to add a new private spawn for a player
     *
     * @author Schmostrong
     * @param p The player who wants to set a new spawn point
     * @param spawnName The name that is assigned to this spawn location
     * @param spawnLoc The location the spawnPoint shall be set
     * @param material The material that is shown in the custom inventory
     */
    public void addPrivateSpawn(Player p, String spawnName, Location spawnLoc, Material material){
        this.privateSpawns.add(new OGSpawn(p, spawnName, spawnLoc, material));
    }

    /**
     * Funtion is used to remove an existing private spawn point
     *
     * @param spawn The spawn point that shall be removed
     */
    public void removePrivateSpawn(OGSpawn spawn){
        this.privateSpawns.remove(spawn);
    }

    public OGSpawn removePrivateSpawnBySpawnName(Player p, String spawnName){
        for(OGSpawn spawn : this.privateSpawns){
            if(spawn.getSpawnName().equals(spawnName) && spawn.getSpawnOwner().equals(p)){
                this.privateSpawns.remove(spawn);
                return spawn;
            }
        }
        return null;
    }

    /**
     * Function is used to retrieve all private spawns of a single player
     *
     * @author Schmostrong
     * @param p The player to retrieve the private spawns from
     * @return All private spawns with their assigned names and exact location
     */
    public List<OGSpawn> getPrivateSpawns(Player p){
        List<OGSpawn> privateSpawnTemp = new LinkedList<>();

        for(OGSpawn spawn : privateSpawns){
            if(spawn.getSpawnOwner().getUniqueId().equals(p.getUniqueId())){
                privateSpawnTemp.add(spawn);
            }
        }

        return privateSpawnTemp;
    }

    public void unloadPlayersData(Player p){
        List<OGSpawn> spawnsToRemove = new LinkedList<>();
        for(OGSpawn spawn : privateSpawns){
            if(spawn.getSpawnOwner().getUniqueId().equals(p.getUniqueId())){
                spawnsToRemove.add(spawn);
            }
        }

        this.privateSpawns.removeAll(spawnsToRemove);
    }

    public List<OGSpawn> getAllPrivateSpawns(){
        return privateSpawns;
    }

    public void addPlayerToConfigProcess(Player p, int index){
        this.currentConfigPage.put(p, index);
    }

    public void removePlayerFromConfigProcess(Player p){
        this.currentConfigPage.remove(p);
    }

    public Integer getCurrentConfigPage(Player p){
        return this.currentConfigPage.get(p);
    }

    public boolean isPlayerInConfigMode(Player p){
        return this.currentConfigPage.containsKey(p);
    }

    public void setPlayerConfigPagePlus(Player p){
        Integer currentConfigPage = this.currentConfigPage.get(p);
        this.currentConfigPage.replace(p, ++currentConfigPage);
    }

    public void setPlayerConfigPageMinus(Player p){
        Integer currentConfigPage = this.currentConfigPage.get(p);
        this.currentConfigPage.replace(p, --currentConfigPage);
    }
}
