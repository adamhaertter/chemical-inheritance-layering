import config.ProjectConfig;
import datasource.BaseDataGateways;
import org.junit.Assert;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class BaseDataGatewaysTest {

    private Connection conn;


    @org.junit.jupiter.api.Test
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = conn.createStatement();
        String insertTestBase = "INSERT into Base VALUES (1, 'TestBase', 2)";
        String insertTestSolute = "INSERT into Base VALUES (2, 'TestBase', 1)";
        stmnt.executeUpdate(insertTestSolute);
        stmnt.executeUpdate(insertTestBase);
    }

    @org.junit.jupiter.api.Test
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    @org.junit.jupiter.api.Test
    void getName() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(1);
        assertEquals("TestBase", base.getName());
        base.delete();
        assertNull(base.getName());
    }

    @org.junit.jupiter.api.Test
    void updateName() {
        BaseDataGateways base = new BaseDataGateways(1);
        base.updateName("UpdatedName");
        assertEquals("UpdatedName", base.getName());
    }

    @org.junit.jupiter.api.Test
    void getSolute() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(1);
        assertEquals(2, base.getSolute());
        base.delete();
        assertEquals(-1, base.getSolute());
    }

    @org.junit.jupiter.api.Test
    void updateSolute() {
        BaseDataGateways base = new BaseDataGateways(1);
        base.updateSolute(3);
        assertEquals(3, base.getSolute());
    }

    @org.junit.jupiter.api.Test
    void persist() throws SQLException {
        BaseDataGateways base = new BaseDataGateways(1);
        // These values are different than the ones we instantiate the tests with
        base.persist(1, "UpdatedSolute", 3);

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

    @org.junit.jupiter.api.Test
    void constructorInsert() throws SQLException {
        BaseDataGateways base = new BaseDataGateways("TestBase2", 2);
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Base WHERE id = 4");
        rs.next();
        String nameFromDB = rs.getString("name");
        long soluteFromDB = rs.getLong("solute");
        assertEquals("TestBase2", nameFromDB);
        assertEquals(2, soluteFromDB);
    }
}