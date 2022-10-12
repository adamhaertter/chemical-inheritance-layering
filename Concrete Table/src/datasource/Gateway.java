package datasource;

import config.ProjectConfig;
import enums.TableEnums;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * General gateway class that has the methods needed in all gateways
 */

public class Gateway {
    protected boolean deleted = false;
    protected long id;
    protected Connection conn;

    public void delete(TableEnums.Table table) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            String delete = "DELETE FROM " + table + " WHERE id = '" + id + "'";
            statement.executeUpdate(delete);
        } catch (Exception ex) {
            System.out.println("Error deleting from database");

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
}
