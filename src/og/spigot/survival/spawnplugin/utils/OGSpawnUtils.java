package og.spigot.survival.spawnplugin.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Player, Map<String, Location>> playerSpawns;
    /**
     * The variable holds the singleton instance of the OGSpawnUtils class
     */
    private static OGSpawnUtils spawnUtils;

    /**
     *
     * Private constructor, so that the singleton design pattern is complete
     */
    private OGSpawnUtils(){
        globalSpawn = null;
        playerSpawns = new HashMap();
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
     */
    public void addPlayerSpawn(Player p, String spawnName, Location spawnLoc){
        if(playerSpawns.containsKey(p)){
            Map<String, Location> spawns = playerSpawns.get(p);
            spawns.put(spawnName, spawnLoc);
            playerSpawns.replace(p, spawns);
        }else{
            Map<String, Location> spawns = new HashMap<>();
            spawns.put(spawnName, spawnLoc);
            playerSpawns.put(p, spawns);
        }
    }

    /**
     * Function is used to retrieve all private spawns of a single player
     *
     * @author Schmostrong
     * @param p The player to retrieve the private spawns from
     * @return All private spawns with their assigned names and exact location
     */
    public Map<String, Location> getPlayerSpawns(Player p){
        if(playerSpawns.containsKey(p)){
            return playerSpawns.get(p);
        }else{
            Map<String, Location> emptyLocations = new HashMap<>();
            return emptyLocations;
        }
    }

    /**
     * Function is used to get all private spawns of all players
     *
     * It is used when all spawns are written into the database on server shutdown
     *
     * @author Schmostrong
     * @return All players with all their private spawns
     */
    public Map<Player, Map<String, Location>> getAllPlayerSpawns(){
        return playerSpawns;
    }

    /**
     * The function is used to unload the players data after they were stored in the database when quitting the server
     *
     * @author Schmostrong
     * @param p - Represents the player, whose data should be unloaded
     */
    public void unloadPlayersData(Player p){
        playerSpawns.remove(p);
    }
}
