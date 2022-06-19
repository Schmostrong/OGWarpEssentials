package og.spigot.survival.spawnplugin.database;

import og.spigot.survival.spawnplugin.utils.OGSpawn;
import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class DAO {
    private DatabaseConnection connection;
    private static DAO dataAccessObject;

    private DAO(DatabaseConnection connection) {
        this.connection = connection;
    }

    public static DAO getDataAccessObject(){
        if(dataAccessObject != null){
            return dataAccessObject;
        }else{
            dataAccessObject = new DAO(new DatabaseConnection("", "", "", "", 2));
            return dataAccessObject;
        }
    }

    public void createDAOConnection(String host, String database, String username, String password, int port){
        dataAccessObject = new DAO(new DatabaseConnection(host, database, username, password, port));
    }

    public void saveGlobalSpawn(){
        try{
            this.connection.openConnection();
            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, "global");
            ps.setString(2, "-");
            ps.setString(3, "Global Spawn");
            ps.setString(4, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getWorld().getName());
            ps.setInt(5, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getBlockX());
            ps.setInt(6, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getBlockY());
            ps.setInt(7, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getBlockZ());
            ps.setString(8, "");
            ps.setTimestamp(9, new java.sql.Timestamp (System.currentTimeMillis ()));

            ps.executeUpdate();
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }

    public void savePrivateSpawn(Player player){
        try{
            this.connection.openConnection();
            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for(OGSpawn spawn : OGSpawnUtils.getOGSpawnUtils().getPrivateSpawns(player)){
                if(spawn.getSpawnOwner().getUniqueId().equals(player.getUniqueId())){
                    ps.setString(1, "private");
                    ps.setString(2, "" + player.getUniqueId());
                    ps.setString(3, spawn.getSpawnName());
                    ps.setString(4, spawn.getSpawnLocation().getWorld().getName());
                    ps.setInt(5, spawn.getSpawnLocation().getBlockX());
                    ps.setInt(6, spawn.getSpawnLocation().getBlockY());
                    ps.setInt(7, spawn.getSpawnLocation().getBlockZ());
                    ps.setString(8, "" + spawn.getSpawnIcon());
                    ps.setTimestamp(9, new java.sql.Timestamp (System.currentTimeMillis ()));

                    ps.executeUpdate();
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }

    public void saveAllPrivateSpawns(){
        try{
            this.connection.openConnection();
            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for(OGSpawn spawn : OGSpawnUtils.getOGSpawnUtils().getAllPrivateSpawns()){
                ps.setString(1, "private");
                ps.setString(2, "" + spawn.getSpawnOwner().getUniqueId());
                ps.setString(3, spawn.getSpawnName());
                ps.setString(4, spawn.getSpawnLocation().getWorld().getName());
                ps.setInt(5, spawn.getSpawnLocation().getBlockX());
                ps.setInt(6, spawn.getSpawnLocation().getBlockY());
                ps.setInt(7, spawn.getSpawnLocation().getBlockZ());
                ps.setString(8, "" + spawn.getSpawnIcon());
                ps.setTimestamp(9, new java.sql.Timestamp (System.currentTimeMillis ()));

                ps.executeUpdate();
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }

    public void removePrivateSpawn(OGSpawn spawn){
        try{
            this.connection.openConnection();
            PreparedStatement ps = this.connection.getConnection().prepareStatement("DELETE FROM OGSpawnPoints WHERE OwnerUUID = ? AND SpawnName = ?");

            ps.setString(1, "" + spawn.getSpawnOwner().getUniqueId());
            ps.setString(2, spawn.getSpawnName());

            ps.executeUpdate();
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }

    public void retrieveGlobalSpawn(){
        try{
            connection.openConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT World, BlockX, BlockY, BlockZ " +
                                                                                    "FROM OGSpawnPoints " +
                                                                                    "WHERE scope = 'global'");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Location loc = new Location(Bukkit.getWorld(rs.getString("World")), rs.getInt("BlockX"), rs.getInt("BlockY"), rs.getInt("BlockZ"));
                OGSpawnUtils.getOGSpawnUtils().setGlobalSpawn(loc);
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }

    public void retrievePlayerSpawns(Player p){
        try{
            connection.openConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT Spawnname, World, BlockX, BlockY, BlockZ, Material " +
                                                            "FROM OGSpawnPoints " +
                                                            "WHERE OwnerUUID = ?");
            ps.setString(1, "" + p.getUniqueId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Location loc = new Location(Bukkit.getWorld(rs.getString("World")), rs.getInt("BlockX"), rs.getInt("BlockY"), rs.getInt("BlockZ"));
                OGSpawnUtils.getOGSpawnUtils().addPrivateSpawn(p, rs.getString(1), loc, Material.getMaterial(rs.getString("Material")));
            }
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }


    public void prepareTables(){
        try{
            this.connection.openConnection();
            PreparedStatement ps = this.connection.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS OGSpawnPoints" +
                                                                                        "(scope varchar(10)," +
                                                                                        "OwnerUUID varchar(100)," +
                                                                                        "SpawnName varchar(50)," +
                                                                                        "World varchar(50)," +
                                                                                        "BlockX int," +
                                                                                        "BlockY int," +
                                                                                        "BlockZ int," +
                                                                                        "Material varchar(50)," +
                                                                                        "TS_modified timestamp, " +
                                                                                        "PRIMARY KEY(OwnerUUID, SpawnName))");
            ps.executeUpdate();
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }
}
