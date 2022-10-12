package datasource;

import config.ProjectConfig;
import enums.TableEnums;
import exceptions.GatewayDeletedException;
import exceptions.GatewayNotFoundException;
import exceptions.SoluteDoesNotExist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class BaseDataGatewaysTest {

    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * This also inserts test data into our DB directly
     * TODO: Create test DB
     * @throws SQLException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestBase = "INSERT INTO Base VALUES (1, 'TestBase', 2)";
        String insertTestSolute1 = "INSERT INTO Base VALUES (2, 'TestSolute1', 1)";
        String insertTestSolute2 = "INSERT INTO Compound VALUES (3, 'TestSolute2')";
        stmnt.executeUpdate(insertTestSolute1);
        stmnt.executeUpdate(insertTestSolute2);
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
     * Tests that we can properly retrieve the name from the class and that it returns the proper value when deleted
     * This uses the data from the setup
     * @throws SQLException
     */
    @Test
    void getName() throws Exception {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertEquals("TestBase", base.getName());
        base.delete(TableEnums.Table.Base);
        assertThrows(GatewayDeletedException.class, base::getName);
    }

    /**
     * Tests that we can use the gateway to update names properly in the gateway and that they get updated in the DB too
     */
    @Test
    void updateName() throws Exception {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.updateName("UpdatedName");
        assertEquals("UpdatedName", base.getName());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Base WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        String nameFromDB = rs.getString("name");
        assertEquals("UpdatedName", nameFromDB);
    }

    /**
     * Tests that we can properly retrieve the solute from the class and that it returns the proper value when deleted
     * This uses the data from the setup
     * @throws SQLException
     */
    @Test
    void getSolute() throws Exception {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertEquals(2, base.getSolute());
        base.delete(TableEnums.Table.Base);
        assertThrows(GatewayDeletedException.class, base::getSolute);
    }

    /**
     * Tests that we can use the gateway to update solutes properly in the gateway
     */
    @Test
    void updateSolute() throws Exception {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.updateSolute(3);
        assertEquals(3, base.getSolute());

        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Base WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        long soluteFromDB = rs.getLong("solute");
        assertEquals(3, soluteFromDB);
    }


    /**
     * Tests that we can properly delete a base from the DB
     * @throws SQLException if there is an error with the DB
     */
    @Test
    public void testBaseDeletedFromDatabase() throws Exception {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.delete(TableEnums.Table.Base);
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Base WHERE id = 1");
        assertFalse(rs.next());
    }

    /**
     * Test that the proper error is thrown when given an invalid solute
     * @throws GatewayNotFoundException if the base is not found
     */
    @Test
    public void testInvalidSolute() throws GatewayNotFoundException {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertThrows(SoluteDoesNotExist.class, () -> base.updateSolute(-1));
    }
}