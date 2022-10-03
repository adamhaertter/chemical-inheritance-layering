package datasource;

import java.sql.Connection;
import java.sql.DriverManager;

public class Gateway {

    protected boolean deleted = false;
    protected Connection conn;

    public Gateway() {
        this.conn = setUpConnection();
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
        }
        return null;
    }

    public void delete() {
        try {
            // TODO code to delete from database
        } catch (Exception e) {
            // throw error about failing to delete
        }
        // does not hit unless sql delete is successful
        this.deleted = true;
    }

    public boolean verifyExistence() {
        return !deleted;
    }
}
