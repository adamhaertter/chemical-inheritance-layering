import config.ProjectConfig;
import datasource.BaseDataGateways;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class BaseDataGatewaysTest {

    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestBase = "INSERT into Base VALUES (1, 'TestBase', 2)";
        String insertTestSolute = "INSERT into Base VALUES (2, 'TestBase', 1)";
        stmnt.executeUpdate(insertTestSolute);
        stmnt.executeUpdate(insertTestBase);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        this.conn.rollback();
        this.conn.close();
    }

    @Test
    void getName() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertEquals("TestBase", base.getName());
        base.delete();
        assertNull(base.getName());
    }

    @Test
    void updateName() {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.updateName(conn, "UpdatedName");
        assertEquals("UpdatedName", base.getName());
    }

    @Test
    void getSolute() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        assertEquals(2, base.getSolute());
        base.delete();
        assertEquals(-1, base.getSolute());
    }

    @Test
    void updateSolute() {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        base.updateSolute(conn, 3);
        assertEquals(3, base.getSolute());
    }

    @Test
    void persist() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(conn, 1);
        // These values are different than the ones we instantiate the tests with
        base.persist(conn, 1, "UpdatedName", 3);

        Statement stmnt = conn.createStatement();
        // We only care about the first entry
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Base WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        String nameFromDB = rs.getString("name");
        long soluteFromDB = rs.getLong("solute");
        assertEquals("UpdatedName", nameFromDB);
        assertEquals(3, soluteFromDB);
    }
}