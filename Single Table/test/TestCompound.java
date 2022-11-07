import datasource.ChemicalDataGateway;
import datasource.CompoundDataGateway;
import datasource.Gateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestCompound {
    private Connection conn = Gateway.setUpConnection();

    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(config.ProjectConfig.DatabaseURL, config.ProjectConfig.DatabaseUser, config.ProjectConfig.DatabasePassword);
        conn.setAutoCommit(false);

        // Insert Test Data
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type)" + " VALUES (1, 'TestCompound', 1, 0, 0, 0, 3, 'Compound')");
        stmt.executeUpdate("INSERT INTO CompoundToElement (compoundId, elementId)" + "" +
                "VALUES (1, 1234)");
        stmt.executeUpdate("INSERT INTO CompoundToElement (compoundId, elementId)" + "" +
                "VALUES (1, 5678)");
        stmt.executeUpdate("INSERT INTO CompoundToElement (compoundId, elementId)" + "" +
                "VALUES (2, 1234)");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        this.conn.rollback();
        this.conn.close();
    }

    /**
     * When initialized by id, a CompoundDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() throws SQLException {
        assertNotNull(conn);

        ChemicalDataGateway chemGW = new ChemicalDataGateway(conn, 1);

        assertEquals(chemGW.getName(), "TestCompound");
    }

    /**
     * Tests that deleting items works
     */
    @Test
    public void testDelete() throws SQLException {
        assertNotNull(conn);

        long trueCompoundId = 1;
        long trueElementId = 1234;

        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO CompoundToElement (compoundId, elementId)" +
                    "VALUES (?, ?)");
            statement.setLong(1, trueCompoundId);
            statement.setLong(2, trueElementId);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        CompoundDataGateway comp = new CompoundDataGateway(conn, trueCompoundId, trueElementId);

        // Does the deleted boolean change?
        assertTrue(comp.verify());
        comp.delete();
        assertFalse(comp.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM CompoundToElement WHERE compoundId = ?");
            statement.setLong(1, trueCompoundId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that getting all compounds will retrieve all the compounds
     * @throws SQLException - for problems that may occur in the database
     */
    @Test
    public void testGetAllCompounds() throws SQLException {

    }

    /**
     * Tests that getting compounds by element id will only return the correct compounds
     * @throws SQLException - for problems that may occur in the database
     */
    @Test
    public void testGetCompoundsByElementID() throws SQLException {
        CompoundDataGateway gw = new CompoundDataGateway(conn, 1, 1234);
        ArrayList<Long> listMethod = gw.getCompoundsContaining(1234);

        ArrayList<Long> testerIDs = new ArrayList<>();
        testerIDs.add((long)1);
        testerIDs.add((long)2);
        ArrayList<Long> methodIDs = new ArrayList<>();
        methodIDs.add(listMethod.get(0));
        methodIDs.add(listMethod.get(1));

        Assertions.assertEquals(testerIDs, methodIDs);
    }

    /**
     * Tests that getting compound elements will return the correct compound elements
     * @throws SQLException - for problems that may occur in the database
     */
    @Test
    public void testGetElementsInCompound() throws SQLException {
        CompoundDataGateway gw = new CompoundDataGateway(conn, 1, 1234);
        ArrayList<Long> listMethod = gw.getElementsInCompound(1);

        ArrayList<Long> testerIDs = new ArrayList<>();
        testerIDs.add((long)1234);
        testerIDs.add((long)5678);
        ArrayList<Long> methodIDs = new ArrayList<>();
        methodIDs.add(listMethod.get(0));
        methodIDs.add(listMethod.get(1));

        Assertions.assertEquals(testerIDs, methodIDs);
    }
}