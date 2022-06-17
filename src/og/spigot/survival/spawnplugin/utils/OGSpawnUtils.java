package og.spigot.survival.spawnplugin.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OGSpawnUtils {
    private Location globalSpawn;
    private Map<Player, Map<String, Location>> playerSpawns;
    private static OGSpawnUtils spawnUtils;

    private OGSpawnUtils(){
        globalSpawn = null;
        playerSpawns = new HashMap();
    }

    public static OGSpawnUtils getOGSpawnUtils(){
        if(spawnUtils != null){
            return spawnUtils;
        }else{
            spawnUtils = new OGSpawnUtils();
            return spawnUtils;
        }
    }

    public void setGlobalSpawn(Location spawnLoc){
        globalSpawn = spawnLoc;
    }

    public Location getGlobalSpawn(){
        return globalSpawn;
    }

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

    public Map<String, Location> getPlayerSpawns(Player p){
        if(playerSpawns.containsKey(p)){
            return playerSpawns.get(p);
        }else{
            Map<String, Location> emptyLocations = new HashMap<>();
            return emptyLocations;
        }
    }
}
