package og.spigot.survival.spawnplugin.database;

import og.spigot.survival.spawnplugin.utils.OGSpawnUtils;
import org.bukkit.Location;
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
            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, "global");
            ps.setString(2, "-");
            ps.setString(3, "Global Spawn");
            ps.setString(4, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getWorld().getName());
            ps.setInt(5, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getBlockX());
            ps.setInt(6, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getBlockY());
            ps.setInt(7, OGSpawnUtils.getOGSpawnUtils().getGlobalSpawn().getBlockZ());
            ps.setTimestamp(8, new java.sql.Timestamp (System.currentTimeMillis ()));

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
            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            for(Map.Entry<String, Location> playerSpawns : OGSpawnUtils.getOGSpawnUtils().getPlayerSpawns(player).entrySet()){
                ps.setString(1, "private");
                ps.setString(2, player.getDisplayName());
                ps.setString(3, playerSpawns.getKey());
                ps.setString(4, playerSpawns.getValue().getWorld().getName());
                ps.setInt(5, playerSpawns.getValue().getBlockX());
                ps.setInt(6, playerSpawns.getValue().getBlockY());
                ps.setInt(7, playerSpawns.getValue().getBlockZ());
                ps.setTimestamp(8, new java.sql.Timestamp (System.currentTimeMillis ()));

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

    public void saveAllPrivateSpawns(){
        try{
            this.connection.openConnection();
            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            for(Map.Entry<Player, Map<String, Location>> playerSpawns : OGSpawnUtils.getOGSpawnUtils().getAllPlayerSpawns().entrySet()){
                ps.setString(1, "private");
                ps.setString(2, playerSpawns.getKey().getDisplayName());

                for(Map.Entry<String, Location> specificPlayerSpawns : playerSpawns.getValue().entrySet()){
                    ps.setString(3, specificPlayerSpawns.getKey());
                    ps.setString(4, specificPlayerSpawns.getValue().getWorld().getName());
                    ps.setInt(5, specificPlayerSpawns.getValue().getBlockX());
                    ps.setInt(6, specificPlayerSpawns.getValue().getBlockY());
                    ps.setInt(7, specificPlayerSpawns.getValue().getBlockZ());
                    ps.setTimestamp(8, new java.sql.Timestamp (System.currentTimeMillis ()));

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

    public void retrieveGlobalSpawn(World world){
        try{
            connection.openConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT BlockX, BlockY, BlockZ " +
                                                                                    "FROM OGSpawnPoints " +
                                                                                    "WHERE scope = 'global'");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Location loc = new Location(world, rs.getInt("BlockX"), rs.getInt("BlockY"), rs.getInt("BlockZ"));
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

    public void retrievePlayerSpawns(Player p, World world){
        try{
            connection.openConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT Spawnname, BlockX, BlockY, BlockZ " +
                                                            "FROM OGSpawnPoints " +
                                                            "WHERE OwnerName = ?");
            ps.setString(1, p.getDisplayName());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Location loc = new Location(world, rs.getInt("BlockX"), rs.getInt("BlockY"), rs.getInt("BlockZ"));
                OGSpawnUtils.getOGSpawnUtils().addPlayerSpawn(p, rs.getString(1), loc);
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
            PreparedStatement ps = this.connection.getConnection().prepareStatement("CREATE TABLE OGSpawnPoints" +
                                                                                        "(scope varchar(10)," +
                                                                                        "OwnerName varchar(30)," +
                                                                                        "SpawnName varchar(50)," +
                                                                                        "World varchar(50)," +
                                                                                        "BlockX int," +
                                                                                        "BlockY int," +
                                                                                        "BlockZ int," +
                                                                                        "TS_modified timestamp, " +
                                                                                        "PRIMARY KEY(OwnerName, SpawnName))");
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
