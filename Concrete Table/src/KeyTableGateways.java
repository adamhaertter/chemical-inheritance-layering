import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Deals with our key table
 */

public class KeyTableGateways extends Gateway {
    private final static AtomicLong key = new AtomicLong();

    /**
     * Get the current key in our DB, this is the next available key, and then increment it and push back
     * so that we can retrieve it on our next call.
     * @return the current key
     */
    public static synchronized long getKey() {
        Connection conn = Gateway.setUpConnection();
        // get current key
        long currentKey = key.get();

        // update the key and push back to the DB

        return currentKey;
    }

    public boolean persists() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE KeyTable SET nextValidId = '" + key.get() + "'");
        } catch (Exception ex) {
            // Fails because already exists?
            return false;
        }
        return true;
    }
}
