package datasource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class TestChemical {

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
        String insertTestChemical = "INSERT INTO Chemical VALUES (1, 'TestChemical')";
        stmnt.executeUpdate(insertTestChemical);
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
     * When initialized by id, a ChemicalDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        ChemicalDataGateway chem = new ChemicalDataGateway(conn, 1);
        // Does it correspond to the right row?
        assertTrue(chem.getName().equals("TestChemical"));
    }

    /**
     * Checks that when a ChemicalDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";
        ChemicalDataGateway chem = new ChemicalDataGateway(conn, trueName);
        // Test that the value is set properly for the Object
        assertTrue(chem.getName().equals(trueName));

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that a ChemicalDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 1;
        String trueName = "TestChemical";

        ChemicalDataGateway chem = new ChemicalDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(chem.verify());
        chem.delete();
        assertFalse(chem.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
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
    public void testUpdateName() {
        assertNotNull(conn);

        String trueName = "TestChemical";
        String tempName = "MyTempName";

        ChemicalDataGateway myChemical = new ChemicalDataGateway(conn, 1);

        // test that the value is set properly for the object
        assertTrue(myChemical.getName().equals(trueName));

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 1");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
        } catch (SQLException e) {
            fail();
        }

        // set name to new name
        myChemical.updateName(tempName);
        assertEquals(tempName, myChemical.getName());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 1");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), tempName);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueName, myChemical.getName());
    }

}
