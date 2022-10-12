package datasource;

import config.ProjectConfig;
import datasource.KeyTableGateways;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class KeyTableGatewaysTest {
    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * TODO: Create test DB
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
    }

    /**
     * Rolls back any changes that we made. Also closes our current connection
     * @throws SQLException
     */
    @AfterEach
    public void tearDown() throws SQLException {
        this.conn.rollback();
        this.conn.close();
    }

    /**
     * Tests that we get the key properly from the method and that it matches the DB value
     * @throws SQLException
     */
    @Test
    public void testGetKey() throws SQLException {
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM KeyTable");
        rs.next();
        long currentKey = rs.getLong("nextValidID");
        assertEquals(currentKey, KeyTableGateways.getNextValidKey(conn));
    }

    /**
     * Tests that the DB value properly increments after we get a key for a solute
     * @throws SQLException
     */
    @Test
    public void testKeyUpdate() throws SQLException {
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM KeyTable");
        rs.next();
        long prevKey = rs.getLong("nextValidID");
        // update the key (increment by 1)
        KeyTableGateways.getNextValidKey(conn);

        // get key again
        rs = stmnt.executeQuery("SELECT * FROM KeyTable");
        rs.next();
        long currentKey = rs.getLong("nextValidID");
        assertEquals(prevKey + 1, currentKey);
    }
}
