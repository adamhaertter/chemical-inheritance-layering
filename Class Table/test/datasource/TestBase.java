package datasource;

import datasource.BaseDataGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class TestBase {

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
        String insertTestChemical = "INSERT INTO Chemical VALUES (1, 'TestBase')";
        String insertTestSolute = "INSERT INTO Chemical VALUES (2, 'TestSolute')";
        String insertTestBase = "INSERT INTO Base VALUES (1, 2)";
        stmnt.executeUpdate(insertTestChemical);
        stmnt.executeUpdate(insertTestSolute);
        stmnt.executeUpdate(insertTestBase);
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
     * When initialized by id, an BaseDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        BaseDataGateway Base = new BaseDataGateway(conn, 1);

        // Does it correspond to the right row?
        assertEquals("TestBase", Base.getName());
        assertEquals(Base.getSolute(), 2);
    }

    /**
     * Checks that when an BaseDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "TestBaseTwo";
        long trueSolute = 2;

        BaseDataGateway Base = new BaseDataGateway(conn, trueName, trueSolute);
        // Test that the value is set properly for the Object
        assertEquals(Base.getName(), trueName);

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Base WHERE solute = ?");

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
     * Ensures that an BaseDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 1;

        BaseDataGateway Base = new BaseDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(Base.verify());
        Base.delete();
        assertFalse(Base.verify());

        // Test remove from Base
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Base WHERE id = ?");
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

        String trueName = "TestBaseTwo";
        long trueSolute = 2;
        long tempSolute = 3;

        Statement stmnt = conn.createStatement();
        String insertTestSolute = "INSERT INTO Chemical VALUES (3, 'AltSolute')";
        stmnt.executeUpdate(insertTestSolute);

        BaseDataGateway myBase = new BaseDataGateway(conn, trueName, trueSolute);
        assertEquals(myBase.getName(), trueName);
        assertEquals(myBase.getSolute(), trueSolute);

        // Test that the value exists in the database
        CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
        statement.setString(1, trueName);
        ResultSet rs = statement.executeQuery();
        rs.next();
        assertEquals(rs.getString("name"), trueName);

        statement = conn.prepareCall("SELECT * from Base WHERE solute = ?");
        statement.setLong(1, trueSolute);
        rs = statement.executeQuery();
        rs.next();
        assertEquals(rs.getLong("solute"), trueSolute);

        // set solute to new value
        myBase.updateSolute(tempSolute);

        // test that the value has changed in the database
        try {
            statement = conn.prepareCall("SELECT * from Base WHERE solute = ?");
            statement.setLong(1, tempSolute);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("solute"), tempSolute);

        } catch (SQLException e) {
            fail();
        }

        // test that the value has changed on our end
        assertNotEquals(trueSolute, myBase.getSolute());
    }

}