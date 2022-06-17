package og.spigot.survival.spawnplugin.database;

import java.sql.PreparedStatement;

public class DAO {
    private DatabaseConnection connection;

    public DAO(DatabaseConnection connection) {
        this.connection = connection;
    }

    public void saveGlobalSpawn(){
        try{
            this.connection.openConnection();

            PreparedStatement ps = this.connection.getConnection().prepareStatement("INSERT INTO OGSpawnPoints VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        }catch (Exception ex){

        }finally {
            if(connection.getConnection() != null){
                connection.closeConnection();
            }
        }
    }
}
