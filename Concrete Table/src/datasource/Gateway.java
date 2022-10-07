package datasource;

import config.ProjectConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * General gateway class that has the methods needed in all gateways
 */

public class Gateway {
    protected boolean deleted = false;
    protected Connection conn;
    protected long id;

    public Gateway() {
        this.conn = setUpConnection();
    }

    /**
     * Returns the connection object so this method can be used in static contexts (Ex: table gateways)
     * @return database connection
     */
    public static Connection setUpConnection() {
        try {
            return DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        } catch (Exception ex) {
            System.out.println("Error connecting to database");
        }
        return null;
    }

    public void delete() throws SQLException {
        try {
            // delete code from DB

        } catch (Exception e) {
            //throw error about delete failure
            return;
        }
        this.deleted = true;
        conn.close();
    }
}
