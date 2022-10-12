package datasource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class TestAcid {

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
        String insertTestChemical = "INSERT INTO Chemical VALUES (1, 'TestAcid')";
        String insertTestSolute = "INSERT INTO Chemical VALUES (2, 'TestSolute')";
        String insertTestAcid = "INSERT INTO Acid VALUES (1, 2)";
        stmnt.executeUpdate(insertTestChemical);
        stmnt.executeUpdate(insertTestSolute);
        stmnt.executeUpdate(insertTestAcid);
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
     * When initialized by id, an AcidDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        AcidDataGateway acid = new AcidDataGateway(conn, 1);

        // Does it correspond to the right row?
        assertEquals("TestAcid", acid.getName());
        assertEquals(acid.getSolute(), 2);
    }

    /**
     * Checks that when an AcidDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "TestAcidTwo";
        long trueSolute = 2;

        AcidDataGateway acid = new AcidDataGateway(conn, trueName, trueSolute);
        // Test that the value is set properly for the Object
        assertEquals(acid.getName(), trueName);

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Acid WHERE solute = ?");

            statement.setLong(1, trueSolute);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("solute"), trueSolute);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Ensures that an AcidDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 1;

        AcidDataGateway acid = new AcidDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(acid.verify());
        acid.delete();
        assertFalse(acid.verify());

        // Test remove from Acid
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Acid WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
            // This should throw an error
        } catch (SQLException e) {
            assertTrue(true);
        }

        // Must be removed from both tables
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
            // This should throw an error
        } catch (SQLException e) {
            assertTrue(true);
        }
    }

    /**
     * ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdateSolute() throws SQLException {
        assertNotNull(conn);

        String trueName = "TestAcidTwo";
        long trueSolute = 2;
        long tempSolute = 3;

        Statement stmnt = conn.createStatement();
        String insertTestSolute = "INSERT INTO Chemical VALUES (3, 'AltSolute')";
        stmnt.executeUpdate(insertTestSolute);

        AcidDataGateway myAcid = new AcidDataGateway(conn, trueName, trueSolute);
        assertEquals(myAcid.getName(), trueName);
        assertEquals(myAcid.getSolute(), trueSolute);

        // Test that the value exists in the database
        CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
        statement.setString(1, trueName);
        ResultSet rs = statement.executeQuery();
        rs.next();
        assertEquals(rs.getString("name"), trueName);

        statement = conn.prepareCall("SELECT * from Acid WHERE solute = ?");
        statement.setLong(1, trueSolute);
        rs = statement.executeQuery();
        rs.next();
        assertEquals(rs.getLong("solute"), trueSolute);

        // set solute to new value
        myAcid.updateSolute(tempSolute);

        // test that the value has changed in the database
        try {
            statement = conn.prepareCall("SELECT * from Acid WHERE solute = ?");
            statement.setLong(1, tempSolute);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("solute"), tempSolute);

        } catch (SQLException e) {
            fail();
        }

        // test that the value has changed on our end
        assertNotEquals(trueSolute, myAcid.getSolute());
    }

}
