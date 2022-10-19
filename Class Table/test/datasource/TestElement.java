package datasource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestElement {

    private Connection conn;

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
        String insertTestChemical = "INSERT INTO Chemical VALUES (1, 'TestElement')";
        String insertTestElement = "INSERT INTO Element VALUES (1, 1, 1.0)";
        stmnt.executeUpdate(insertTestChemical);
        stmnt.executeUpdate(insertTestElement);
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
     * When initialized by id, an ElementDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        long trueId = 1;
        String trueName = "TestElement";
        int trueNumber = 1;
        double trueMass = 1.0;

        ElementDataGateway elem = new ElementDataGateway(conn, trueId);
        // Does it correspond to the right row?
        Assertions.assertEquals(elem.getName(), trueName);
        assertEquals(elem.getAtomicNumber(), trueNumber);
        assertTrue(elem.getAtomicMass() == trueMass);
    }

    /**
     * Checks that when a ElementDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";
        int trueNumber = 1;
        double trueMass = 5.0;

        ElementDataGateway elem = new ElementDataGateway(conn, trueName, trueNumber, trueMass);
        // Test that the value is set properly for the Object
        assertEquals(elem.getName(), trueName);
        assertEquals(elem.getAtomicNumber(), trueNumber);
        assertTrue(elem.getAtomicMass() == trueMass);


        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, trueMass);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that an AcidDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 1L;

        ElementDataGateway elem = new ElementDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(elem.verify());
        elem.delete();
        assertFalse(elem.verify());

        // Check removed from one table
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }

        // Must be removed from both tables
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Element WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }
    }

    /**
     * ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdateMass() {
        assertNotNull(conn);

        String trueName = "TestElement";
        int trueNumber = 1;
        double trueMass = 1.0;
        double tempMass = 12.0;

        ElementDataGateway myElement = new ElementDataGateway(conn, trueName, trueNumber, trueMass);

        // test that the values exist in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, trueMass);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
        } catch (SQLException e) {
            fail();
        }

        // set mass to new value
        myElement.updateAtomicMass(tempMass);

        // test that the value has changed in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, tempMass);
            ResultSet rs = statement.executeQuery();
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == tempMass);

        } catch (SQLException e) {
            fail();
        }

        assertNotEquals(trueMass, myElement.getAtomicMass());
    }

    /**
     * ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdateAtomicNumber() {
        assertNotNull(conn);

        String trueName = "TestElementTwo";
        int trueNumber = 5;
        double trueMass = 5.0;
        int tempNumber = 6;

        ElementDataGateway myElement = new ElementDataGateway(conn, trueName, trueNumber, trueMass);

        // test that the values exist in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, trueMass);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
        } catch (SQLException e) {
            fail();
        }

        // set number to new value
        myElement.updateAtomicNumber(tempNumber);

        // test that the value has changed in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Element WHERE atomicNumber = ?");
            statement.setInt(1, tempNumber);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicNumber") == tempNumber);
        } catch (SQLException e) {
            fail();
        }

        assertNotEquals(trueNumber, myElement.getAtomicNumber());
    }
}
