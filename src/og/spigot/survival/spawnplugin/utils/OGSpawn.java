package og.spigot.survival.spawnplugin.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OGSpawn {

    private Player spawnOwner;
    private String spawnName;
    private Location spawnLocation;
    private Material spawnIcon;

    public OGSpawn(Player spawnOwner, String spawnName, Location spawnLocation, Material spawnIcon) {
        this.spawnOwner = spawnOwner;
        this.spawnName = spawnName;
        this.spawnLocation = spawnLocation;
        this.spawnIcon = spawnIcon;
    }

    public Player getSpawnOwner() {
        return spawnOwner;
    }

    public void setSpawnOwner(Player spawnOwner) {
        this.spawnOwner = spawnOwner;
    }

    public String getSpawnName() {
        return spawnName;
    }

    public void setSpawnName(String spawnName) {
        this.spawnName = spawnName;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Material getSpawnIcon() {
        return spawnIcon;
    }

    public void setSpawnIcon(Material spawnIcon) {
        this.spawnIcon = spawnIcon;
    }
}
