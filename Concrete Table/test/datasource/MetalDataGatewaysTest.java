package datasource;

import config.ProjectConfig;
import datasource.enums.TableEnums;
import datasource.exceptions.GatewayDeletedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class MetalDataGatewaysTest {
    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * Also inserts our testing data
     * TODO: Create test DB
     * @throws SQLException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestMetal = "INSERT INTO Metal VALUES (1, 'TestMetal', 10, 12.5, 2)";
        String insertTestAcid = "INSERT INTO Acid VALUES (2, 'TestAcid', 3)";
        stmnt.executeUpdate(insertTestAcid);
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
     * Create a new acid and check that it is created properly
     * @throws GatewayDeletedException if the gateway is deleted
     */
    @Test
    public void createNewMetal() throws GatewayDeletedException {
        MetalDataGateways metal = new MetalDataGateways(conn, "NewMetal", 15, 20.3, 2);
        assertEquals("NewMetal", metal.getName());
        assertEquals(15, metal.getAtomicNumber());
        assertEquals(20.3, metal.getAtomicMass(), 0.01);
        assertEquals(2, metal.getDissolvedBy());
    }

    /**
     * Checks that we can get the name properly after making a connection to the DB and that it returns the proper value
     * when deleted
     * @throws SQLException
     */
    @Test
    public void getName() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        assertEquals("TestMetal", metal.getName());
        metal.delete(TableEnums.Table.Metal);
        assertThrows(GatewayDeletedException.class , metal::getName);
    }

    /**
     * Checks that we can update the name in our gateway object as well as the DB
     * @throws SQLException
     */
    @Test
    public void updateName() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        metal.updateName("UpdatedName");
        assertEquals("UpdatedName", metal.getName());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Metal WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        String nameFromDB = rs.getString("name");
        assertEquals("UpdatedName", nameFromDB);
    }

    /**
     * Tests that we can get the current atomic number in our gateway object and that it returns the proper value
     * when deleted
     * @throws SQLException
     */
    @Test
    public void getAtomicNumber() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        assertEquals(10, metal.getAtomicNumber());
        metal.delete(TableEnums.Table.Metal);
        assertThrows(GatewayDeletedException.class , metal::getAtomicNumber);
    }

    /**
     * Tests that we can update the atomic number in our gateway object and the DB
     * @throws SQLException
     */
    @Test
    public void updateAtomicNumber() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        metal.updateAtomicNumber(13);
        assertEquals(13, metal.getAtomicNumber());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Metal WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        int atomicNumberFromDB = rs.getInt("atomicNumber");
        assertEquals(13, atomicNumberFromDB);
    }

    /**
     * Tests that we can get our current atomic mass and that it returns the proper value when deleted
     * @throws SQLException
     */
    @Test
    public void getAtomicMass() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        assertEquals(12.5, metal.getAtomicMass(),0.01);
        metal.delete(TableEnums.Table.Metal);
        assertThrows(GatewayDeletedException.class , metal::getAtomicMass);
    }

    /**
     * Tests that we can properly update the atomic mass in our gateway object and the DB
     * @throws SQLException
     */
    @Test
    public void updateAtomicMass() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        metal.updateAtomicMass(13.7);
        assertEquals(13.7, metal.getAtomicMass(), 0.01);

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Metal WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        double atomicMassFromDB = rs.getDouble("atomicMass");
        assertEquals(13.7, atomicMassFromDB, 0.01);
    }

    /**
     * Tests that we can properly get the current dissolvedBy in our getway and that it returns the proper value
     * when deleted
     * @throws SQLException
     */
    @Test
    public void getDissolvedBy() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        assertEquals(2, metal.getDissolvedBy());
        metal.delete(TableEnums.Table.Metal);
        assertThrows(GatewayDeletedException.class , metal::getDissolvedBy);
    }

    /**
     *
     * @throws SQLException
     */
    @Test
    public void updateDissolvedBy() throws Exception {
        MetalDataGateways metal = new MetalDataGateways(conn, 1);
        // We don't have the foreign key in here yet, so it shouldn't update the metal
        metal.updateDissolvedBy(4);
        assertEquals(2, metal.getDissolvedBy());

        Statement stmnt = conn.createStatement();
        stmnt.executeUpdate("INSERT INTO Acid VALUES (4, 'TestAcid2', 3)");
        metal.updateDissolvedBy(4);
        assertEquals(4, metal.getDissolvedBy());

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Metal WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        long dissolvedByFromDB = rs.getLong("dissolvedBy");
        assertEquals(4, dissolvedByFromDB);
    }

    /**
     * Tests that we can properly delete a metal from the DB
     * @throws SQLException if there is an error with the DB
     */
    @Test
    public void testMetalDeletedFromDatabase() throws Exception {
        MetalDataGateways element = new MetalDataGateways(conn, 1);
        element.delete(TableEnums.Table.Metal);
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Metal WHERE id = 1");
        assertFalse(rs.next());
    }
}
