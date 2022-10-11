import config.ProjectConfig;
import datasource.BaseDataGateways;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class BaseDataGatewaysTest {

    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * This also inserts test data into our DB directly
     * TODO: Create test DB
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestBase = "INSERT into Base VALUES (1, 'TestBase', 2)";
        String insertTestSolute = "INSERT into Base VALUES (2, 'TestBase', 1)";
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
     * Tests that we can properly retrieve the name from the class and that it returns the proper value when deleted
     * This uses the data from the setup
     * @throws SQLException
     */
    @Test
    void getName() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertEquals("TestBase", base.getName());
        base.delete();
        assertNull(base.getName());
    }

    /**
     * Tests that we can use the gateway to update names properly in the gateway
     */
    @Test
    void updateName() {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.updateName(conn, "UpdatedName");
        assertEquals("UpdatedName", base.getName());
    }

    /**
     * Tests that we can properly retrieve the solute from the class and that it returns the proper value when deleted
     * This uses the data from the setup
     * @throws SQLException
     */
    @Test
    void getSolute() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertEquals(2, base.getSolute());
        base.delete();
        assertEquals(-1, base.getSolute());
    }

    /**
     * Tests that we can use the gateway to update solutes properly in the gateway
     */
    @Test
    void updateSolute() {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.updateSolute(conn, 3);
        assertEquals(3, base.getSolute());
    }
}