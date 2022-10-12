package datasource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;

import static org.junit.Assert.*;

public class TestCompound {

    private Connection conn = Gateway.setUpConnection();

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * Also inserts our testing data
     * @throws SQLException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(config.ProjectConfig.DatabaseURL, config.ProjectConfig.DatabaseUser, config.ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestChemical = "INSERT INTO Chemical VALUES (1, 'TestCompound')";
        String insertTestCompound = "INSERT INTO Compound VALUES (1)";
        stmnt.executeUpdate(insertTestChemical);
        stmnt.executeUpdate(insertTestCompound);
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
     * When initialized by id, a CompoundDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        CompoundDataGateway com = new CompoundDataGateway(conn, 1);
        // Does it correspond to the right row?
        assertEquals(com.getName(), "TestCompound");
    }

    /**
     * Checks that when a CompoundDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";

        CompoundDataGateway com = new CompoundDataGateway(conn, trueName);
        // Test that the value is set properly for the Object
        assertEquals(com.getName(), trueName);

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            long id = rs.getLong("id");

            statement = conn.prepareCall("SELECT * from Compound WHERE id = ?");
            statement.setLong(1, id);
            statement.executeQuery();
            // By not throwing an Error, we know that there is at least one row with this id.
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that a CompoundDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 1;
        String trueName = "TestCompound";

        CompoundDataGateway com = new CompoundDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(com.verify());
        com.delete();
        assertFalse(com.verify());

        // Check compound table
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Compound WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }

        // Must be removed from both tables
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }
    }
}
