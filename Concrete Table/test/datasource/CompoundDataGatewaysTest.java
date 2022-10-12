package datasource;

import config.ProjectConfig;
import dto.CompoundToElementDTO;
import dto.ElementDTO;
import enums.TableEnums;
import exceptions.GatewayDeletedException;
import exceptions.GatewayNotFoundException;
import exceptions.SoluteDoesNotExist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CompoundDataGatewaysTest {
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
        String insertTestCompound = "INSERT INTO Compound VALUES (1, 'TestCompound')";
        String insertTestElement1 = "INSERT INTO Element VALUES (2, 'TestElement1', 10, 10.5)";
        String insertTestElement2 = "INSERT INTO Element VALUES (3, 'TestElement1', 20, 20.5)";
        String insertTestElement3 = "INSERT INTO Element VALUES (6, 'NewElement', 10, 17.5)";
        String insertTestRelation1 = "INSERT INTO CompoundToElement VALUES (1, 2)";
        String insertTestRelation2 = "INSERT INTO CompoundToElement VALUES (1, 3)";
        stmnt.executeUpdate(insertTestCompound);
        stmnt.executeUpdate(insertTestElement1);
        stmnt.executeUpdate(insertTestElement2);
        stmnt.executeUpdate(insertTestElement3);
        stmnt.executeUpdate(insertTestRelation1);
        stmnt.executeUpdate(insertTestRelation2);
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
     * Create a new compound and check that it is created properly
     * @throws GatewayDeletedException if the gateway is deleted
     */
    @Test
    public void createNewCompound() throws GatewayDeletedException {
        CompoundDataGateways compound = new CompoundDataGateways(conn, "NewCompound");
        assertEquals("NewCompound", compound.getName());
    }

    /**
     * Checks that we can get the name properly after making a connection to the DB and that it returns the proper value
     * when deleted
     * @throws SQLException
     */
    @Test
    public void getName() throws Exception {
        CompoundDataGateways compound = new CompoundDataGateways(conn, 1);
        assertEquals("TestCompound", compound.getName());
        compound.delete(TableEnums.Table.Compound);
        assertThrows(GatewayDeletedException.class, compound::getName);
    }

    /**
     * Checks that we can update the name in our gateway object as well as the DB
     * @throws SQLException
     */
    @Test
    public void updateName() throws Exception {
        CompoundDataGateways compound = new CompoundDataGateways(conn, 1);
        compound.updateName("UpdatedName");
        assertEquals("UpdatedName", compound.getName());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Compound WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        String nameFromDB = rs.getString("name");
        assertEquals("UpdatedName", nameFromDB);
    }

    /**
     * Tests that we can properly delete a compound from the DB
     * @throws SQLException if there is an error with the DB
     */
    @Test
    public void testCompoundDeletedFromDatabase() throws Exception {
        CompoundDataGateways compound = new CompoundDataGateways(conn, 1);
        compound.delete(TableEnums.Table.Compound);
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Compound WHERE id = 1");
        assertFalse(rs.next());
    }

    /**
     * Test that we can properly get all the elements from the DB for this compound
     */
    @Test
    public void testGetElements() {
        ArrayList<CompoundToElementDTO> elements = CompoundDataGateways.getAllElementsInCompound(conn, 1);
        assertEquals(2, elements.size());
        assertEquals(2, elements.get(0).elementId);
        assertEquals(3, elements.get(1).elementId);
    }

    /**
     * Test that we can properly add an element to compound relation to the DB
     * @throws GatewayNotFoundException if the gateway is not found
     */
    @Test
    public void testAddElementToCompound() throws GatewayNotFoundException {
        CompoundDataGateways compound = new CompoundDataGateways(conn, 1);
        compound.addElement(6);
        ArrayList<CompoundToElementDTO> compounds = CompoundDataGateways.getAllElementsInCompound(conn, 1);
        assertEquals(3, compounds.size());
        assertEquals(6, compounds.get(2).elementId);
    }
}
