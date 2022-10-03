import config.ProjectConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class Gateway {
    protected boolean deleted = false;
    protected Connection conn;

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

    public void delete() {
        try {
            // delete code from DB
        } catch (Exception e) {
            //throw error about delete failure
        }
        this.deleted = true;
    }
}