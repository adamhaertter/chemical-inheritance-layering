package datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Deals with our key table which manages the next available unique identifier for primary key generation in our database.
 */
public class KeyTableGateway extends Gateway {

    /**
     * Creates a gateway on the KeyTable with the given connection
     *
     * @param conn the db connection
     */
    public KeyTableGateway(Connection conn) {
        super(conn);
    }

    /**
     * Get the current key in our DB, this is the next available key, and then increment it and push back
     * so that we can retrieve it on our next call.
     * @return the current key
     */
    public static synchronized long getNextValidKey(Connection conn) {
        // get current key and then set the value in key to the next valid id
        long nextValidKey = -1;

        try {
            assert conn != null;
            Statement statement = conn.createStatement();
            // Get the next valid key from the DB
            ResultSet rs = statement.executeQuery("SELECT * FROM KeyTable");
            rs.next();
            nextValidKey = rs.getLong("nextValidId");

            // Increment the key in the DB
            String updateKey = "UPDATE KeyTable SET nextValidId = '" + (nextValidKey + 1) + "'";
            statement.executeUpdate(updateKey);
        } catch (Exception ex) {
            //key didn't insert because already in db?
        }
        return nextValidKey;
    }
}

