package datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Contains basic functionality to be used by all Gateways. All RowDataGateway/TableDataGateway combined classes should
 * extend this class or a class that inherits these methods to inherit basic delete and connection functionality.
 */
public class Gateway {

    protected long id;
    protected boolean deleted = false;
    protected Connection conn;
    private static ArrayList<Connection> openConnections = new ArrayList<>();

    /**
     * Basic Gateway constructor which calls to set up database connectivity for all child gateways. All children
     * should super(Connection) to inherit this connection.
     *
     * @param conn connection to the DB
     */
    public Gateway(Connection conn) {
        this.conn = conn;
    }

    /**
     * Creates DB Connection and returns the connection object for use in static contexts
     * @return database connection
     */
    public static Connection setUpConnection() {
        try {
            Connection c = DriverManager.getConnection(config.ProjectConfig.DatabaseURL,
                    config.ProjectConfig.DatabaseUser, config.ProjectConfig.DatabasePassword);
            openConnections.add(c);
            return c;
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
            CallableStatement statement = conn.prepareCall("DELETE FROM Chemical WHERE id = ?");
            // We only delete from Chemical because the Foreign Keys cascade on delete, removing it from all tables
            statement.setLong(1, id);
            statement.execute();
        } catch (Exception e) {
            // throw error about failing to delete
            e.printStackTrace();
        }
        // does not hit unless sql delete is successful
        this.deleted = true;
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the unique identifier of the object this gateway represents
     * @return the id
     */
    public long getId() {
        return id;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //openConnections.remove(conn);
    }

    public static void closeAllConnections() {
        for(Connection c : openConnections){
            try {
                c.close();
            } catch (SQLException e) {
                // oh well
            }
        }
        openConnections.removeAll(openConnections);
    }
}
