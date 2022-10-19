package datasource;

import config.ProjectConfig;
import datasource.dto.MetalDTO;
import datasource.enums.TableEnums;
import datasource.exceptions.GatewayDeletedException;
import datasource.exceptions.GatewayNotFoundException;
import datasource.exceptions.SoluteDoesNotExist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AcidDataGatewaysTest {
    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * Also inserts our testing data
     * TODO: Create test DB
     *
     * @throws SQLException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestAcid = "INSERT INTO Acid VALUES (1, 'TestAcid', 3)";
        String insertTestMetal1 = "INSERT INTO Metal VALUES (2, 'TestMetal1', 10, 12.5, 1)";
        String insertTestMetal2 = "INSERT INTO Metal VALUES (3, 'TestMetal2', 13, 14.5, 1)";
        stmnt.executeUpdate(insertTestAcid);
        stmnt.executeUpdate(insertTestMetal1);
        stmnt.executeUpdate(insertTestMetal2);
    }

    /**
     * Rolls back any changes that we made. Also closes our current connection
     *
     * @throws SQLException
     */
    @AfterEach
    public void tearDown() throws SQLException {
        this.conn.rollback();
        this.conn.close();
    }

    /**
     * Create a new acid and check that it is created properly
     * @throws SoluteDoesNotExist if the solute does not exist
     * @throws GatewayDeletedException if the gateway is deleted
     */
    @Test
    public void createNewAcid() throws SoluteDoesNotExist, GatewayDeletedException {
        AcidDataGateways acid = new AcidDataGateways(conn, "NewAcid", 3);
        assertEquals("NewAcid", acid.getName());
        assertEquals(3, acid.getSolute());
    }

    /**
     * Checks that we can get the name properly after making a connection to the DB and that it returns the proper value
     * when deleted
     *
     * @throws SQLException
     */
    @Test
    public void getName() throws Exception {
        AcidDataGateways acid = new AcidDataGateways(conn, 1);
        assertEquals("TestAcid", acid.getName());
        acid.delete(TableEnums.Table.Acid);
        assertThrows(GatewayDeletedException.class, acid::getName);
    }

    /**
     * Tests that we can update the name field in both the gateway object and the DB
     *
     * @throws SQLException
     */
    @Test
    public void updateName() throws Exception {
        AcidDataGateways acid = new AcidDataGateways(conn, 1);
        acid.updateName("UpdatedName");
        assertEquals("UpdatedName", acid.getName());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Acid WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        String nameFromDB = rs.getString("name");
        assertEquals("UpdatedName", nameFromDB);
    }

    /**
     * Tests that we can get the solute value from the gateway and that it returns the proper value when deleted
     *
     * @throws SQLException
     */
    @Test
    public void getSolute() throws Exception {
        AcidDataGateways acid = new AcidDataGateways(conn, 1);
        assertEquals(3, acid.getSolute());
        acid.delete(TableEnums.Table.Acid);
        assertThrows(GatewayDeletedException.class, acid::getSolute);
    }

    /**
     * Tests that we can update a solute in our object and the DB
     *
     * @throws SQLException
     */
    @Test
    public void updateSolute() throws Exception {
        AcidDataGateways acid = new AcidDataGateways(conn, 1);
        acid.updateSolute(2);
        assertEquals(2, acid.getSolute());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Acid WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        long soluteFromDB = rs.getLong("solute");
        assertEquals(2, soluteFromDB);
    }

    /**
     * Tests that we can properly get the metals that are dissolved by an acid
     *
     * @throws SQLException
     */
    @Test
    public void getDissolvedMetals() {
        //Dissolved metals in the setup are ids 2 and 3
        ArrayList<MetalDTO> metals = AcidDataGateways.getDissolvedMetals(conn, 1);
        assertEquals(2, metals.size());
        // make sure that the DTOs were made properly with the right things
        assertEquals(2, metals.get(0).id);
        assertEquals("TestMetal1", metals.get(0).name);
        assertEquals(10, metals.get(0).atomicNumber);
        assertEquals(12.5, metals.get(0).atomicMass, 0.01);
        assertEquals(1, metals.get(0).dissolvedBy);
        assertEquals(3, metals.get(1).id);
        assertEquals("TestMetal2", metals.get(1).name);
        assertEquals(13, metals.get(1).atomicNumber);
        assertEquals(14.5, metals.get(1).atomicMass);
        assertEquals(1, metals.get(1).dissolvedBy);
    }


    /**
     * Tests that we can properly delete an acid from the DB
     *
     * @throws SQLException if there is an error with the DB
     */
    @Test
    public void testMetalDeletedFromDatabase() throws Exception {
        AcidDataGateways acid = new AcidDataGateways(conn, 1);
        acid.delete(TableEnums.Table.Acid);
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Acid WHERE id = 1");
        assertFalse(rs.next());
    }

    /**
     * Test that the proper error is thrown when given an invalid solute
     * @throws GatewayNotFoundException if the acid is not found
     */
    @Test
    public void testInvalidSolute() throws GatewayNotFoundException {
        AcidDataGateways acid = new AcidDataGateways(conn, 1);
        assertThrows(SoluteDoesNotExist.class, () -> acid.updateSolute(-1));
    }
}
