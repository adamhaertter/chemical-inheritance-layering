package datasource;

import config.ProjectConfig;
import dto.CompoundToElementDTO;
import enums.TableEnums;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ElementDataGatewaysTest {
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
        String insertTestElement = "INSERT INTO Element VALUES (1, 'TestElement', 5, 7.5)";
        String insertTestCompound1 = "INSERT INTO Compound VALUES (2, 'TestCompound')";
        String insertTestCompound2 = "INSERT INTO Compound VALUES (3, 'TestCompound')";
        String insertTestRelation1 = "INSERT INTO CompoundToElement VALUES (2, 1)";
        String insertTestRelation2 = "INSERT INTO CompoundToElement VALUES (3, 1)";
        stmnt.executeUpdate(insertTestElement);
        stmnt.executeUpdate(insertTestCompound1);
        stmnt.executeUpdate(insertTestCompound2);
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
     * Checks that we can get the name properly after making a connection to the DB and that it returns the proper value
     * when deleted
     * @throws SQLException
     */
    @Test
    public void getName() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        assertEquals("TestElement", element.getName());
        element.delete(TableEnums.Table.Element);
        assertNull(element.getName());
    }

    /**
     * Checks that we can update the name in our gateway object as well as the DB
     * @throws SQLException
     */
    @Test
    public void updateName() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        element.updateName("UpdatedName");
        assertEquals("UpdatedName", element.getName());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Element WHERE id = 1");
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
    public void getAtomicNumber() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        assertEquals(5, element.getAtomicNumber());
        element.delete(TableEnums.Table.Element);
        assertEquals(-1, element.getAtomicNumber());
    }

    /**
     * Tests that we can update the atomic number in our gateway object and the DB
     * @throws SQLException
     */
    @Test
    public void updateAtomicNumber() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        element.updateAtomicNumber(9);
        assertEquals(9, element.getAtomicNumber());

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Element WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        int atomicNumberFromDB = rs.getInt("atomicNumber");
        assertEquals(9, atomicNumberFromDB);
    }

    /**
     * Tests that we can get our current atomic mass and that it returns the proper value when deleted
     * @throws SQLException
     */
    @Test
    public void getAtomicMass() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        assertEquals(7.5, element.getAtomicMass(),0.01);
        element.delete(TableEnums.Table.Element);
        assertEquals(-1, element.getAtomicMass(), 0.01);
    }

    /**
     * Tests that we can properly update the atomic mass in our gateway object and the DB
     * @throws SQLException
     */
    @Test
    public void updateAtomicMass() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        element.updateAtomicMass(10.1);
        assertEquals(10.1, element.getAtomicMass(), 0.01);

        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt.executeQuery("SELECT * FROM Element WHERE id = 1");
        rs.next();
        // check to make sure they updated in the DB
        double atomicMassFromDB = rs.getDouble("atomicMass");
        assertEquals(10.1, atomicMassFromDB, 0.01);
    }

    /**
     * Tests that we can properly delete an element from the DB
     * @throws SQLException if there is an error with the DB
     */
    @Test
    public void testElementDeletedFromDatabase() throws SQLException {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        element.delete(TableEnums.Table.Element);
        Statement stmnt = conn.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM Element WHERE id = 1");
        assertFalse(rs.next());
    }

    /**
     * Test that we can get all the compounds that contain this element
     */
    @Test
    public void testGetCompounds() {
        ElementDataGateways element = new ElementDataGateways(conn, 1);
        ArrayList<CompoundToElementDTO> compounds = element.getAllCompoundsWithThisElement();
        assertEquals(2, compounds.size());
        assertEquals(2, compounds.get(0).compoundId);
        assertEquals(3, compounds.get(1).compoundId);
    }
}
