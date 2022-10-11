package datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Gateway {

    protected long id;
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
            //Class.forName("com.mysql.cj.jdbc.Driver");
            // TODO "No suitable driver found"
            return DriverManager.getConnection(config.ProjectConfig.DatabaseURL, config.ProjectConfig.DatabaseUser, config.ProjectConfig.DatabasePassword);
        } catch (Exception ex) {
            System.out.println("Error connecting to database");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes the given instance from the Chemical table on the basis of the id instance variable. Since there should
     * be no instances of Gateway on its own, the id will be filled by subclasses.
     */
    public void delete() {
        try {
            // TODO code to delete from database
            CallableStatement statement = conn.prepareCall("DELETE FROM Chemical WHERE id = ?");
            // We only delete from Chemical because the Foreign Keys cascade on delete, removing it from all tables
            statement.setLong(1, id);
            statement.execute();
            // close db connection if successful
            conn.close();
        } catch (Exception e) {
            // throw error about failing to delete
            e.printStackTrace();
        }
        // does not hit unless sql delete is successful
        this.deleted = true;
    }
}
