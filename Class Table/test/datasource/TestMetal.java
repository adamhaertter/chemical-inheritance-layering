package datasource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestMetal {

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

        // Create rows relating to the acid
        String insertTestAcidChemical = "INSERT INTO Chemical VALUES (1, 'TestAcid')";
        String insertTestSolute = "INSERT INTO Chemical VALUES (2, 'TestSolute')";
        String insertTestAcid = "INSERT INTO Acid VALUES (1, 2)";
        stmnt.executeUpdate(insertTestAcidChemical);
        stmnt.executeUpdate(insertTestSolute);
        stmnt.executeUpdate(insertTestAcid);

        // Create rows relating to the metal
        String insertTestMetalChemical = "INSERT INTO Chemical VALUES (3, 'TestMetal')";
        String insertTestElement = "INSERT INTO Element VALUES (3, 1, 1.0)";
        String insertTestMetal = "INSERT INTO Metal VALUES (3, 1)";
        stmnt.executeUpdate(insertTestMetalChemical);
        stmnt.executeUpdate(insertTestElement);
        stmnt.executeUpdate(insertTestMetal);
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
     * When initialized by id, a MetalDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        MetalDataGateway metal = new MetalDataGateway(conn, 3);
        // Does it correspond to the right row?
        assertEquals(metal.getName(), "TestMetal");
        assertEquals(metal.getAtomicNumber(), 1);
        assertTrue(metal.getAtomicMass() == 1.0);
    }

    /**
     * Checks that when a MetalDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "TestMetalTwo";
        int trueNumber = 7;
        double trueMass = 7.0;
        long trueDissolve = 1;

        MetalDataGateway metal = new MetalDataGateway(conn, trueName, trueNumber, trueMass, trueDissolve);
        // Test that the value is set properly for the Object
        assertEquals(metal.getName(), trueName);
        assertEquals(metal.getAtomicNumber(), trueNumber);
        assertTrue(metal.getAtomicMass() == trueMass);
        assertEquals(metal.getDissolvedBy(), trueDissolve);


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
            assertTrue(rs.getInt("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);

            statement = conn.prepareCall("SELECT * from Metal WHERE dissolvedBy = ?");
            statement.setLong(1, trueDissolve);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("dissolvedBy"), trueDissolve);
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that an MetalDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 3;
        MetalDataGateway metal = new MetalDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(metal.verify());
        metal.delete();
        assertFalse(metal.verify());


        CallableStatement statement;

        // Must be removed from all 3 tables
        try {
            statement = conn.prepareCall("SELECT * FROM Acid WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }

        try {
            statement = conn.prepareCall("SELECT * FROM Element WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }

        try {
            statement = conn.prepareCall("SELECT * FROM Metal WHERE id = ?");
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
    public void testUpdateDissolve() {
        assertNotNull(conn);

        MetalDataGateway myMetal = new MetalDataGateway(conn, 3);

        // update to new values
        int tempNumber = 10;
        double tempMass = 11.0;
        long trueDissolve = 8;
        myMetal.updateAtomicNumber(tempNumber);
        myMetal.updateAtomicMass((tempMass));
        myMetal.updateDissolvedBy(trueDissolve);

        // test that the values have changed in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = ?");
            statement.setLong(1, 3);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), "TestMetal");

            statement = conn.prepareCall("SELECT * from Element WHERE id = ?");
            statement.setLong(1, 3);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == tempMass);
            assertEquals(rs.getInt("atomicNumber"), tempNumber);

        } catch (SQLException e) {
            fail();
        }

        // test that the value has changed on our end
        assertNotEquals(1.0, myMetal.getAtomicMass());
        assertNotEquals(1, myMetal.getAtomicNumber());

    }
}

