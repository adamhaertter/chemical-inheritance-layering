package datasource;

import datasource.enums.TableEnums;
import datasource.exceptions.GatewayFailedToDelete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * General gateway class that has the methods needed in all gateways
 */

public class Gateway {
    protected boolean deleted = false;
    protected long id;
    protected Connection conn;

    public void delete(TableEnums.Table table) throws GatewayFailedToDelete {
        try {
            Statement statement = conn.createStatement();
            String delete = "DELETE FROM " + table + " WHERE id = '" + id + "'";
            statement.executeUpdate(delete);
        } catch (Exception ex) {
            throw new GatewayFailedToDelete("Failed to delete " + table + " with id " + id, ex);
        }
        this.deleted = true;
    }

    /**
     * Update the connection object
     * @param conn the new connection object
     */
    public void updateConnection(Connection conn) {
        this.conn = conn;
    }



    /**
     * Creates DB Connection and returns the connection object for use in static contexts
     * @return database connection
     */
    public static Connection setUpConnection() {
        try {
            return DriverManager.getConnection(config.ProjectConfig.DatabaseURL, config.ProjectConfig.DatabaseUser, config.ProjectConfig.DatabasePassword);
        } catch (Exception ex) {
            System.out.println("Error connecting to database");
            ex.printStackTrace();
        }
        return null;
    }
}
