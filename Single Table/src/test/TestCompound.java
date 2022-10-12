import datasource.ChemicalDataGateway;
import datasource.CompoundDataGateway;
import datasource.Gateway;
import dto.ChemicalDTO;
import dto.CompoundToElementDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCompound {
    private final Connection conn = Gateway.setUpConnection();

    @BeforeEach
    public void setUp() throws SQLException {
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
        conn.rollback();
        conn.close();
    }

    /**
     * When initialized by id, a CompoundDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() throws SQLException {
        assertNotNull(conn);

        CompoundDataGateway com = new CompoundDataGateway(conn, 1);
        // Does it correspond to the right row?
        assertEquals(com.getCompoundID(), 1);
        assertEquals(com.getElementID(), 1234);
        assertEquals(com.getName(), "TestCompound");
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

        CompoundDataGateway chem = new CompoundDataGateway(conn, trueCompoundId);

        // Does the deleted boolean change?
        assertTrue(chem.verify());
        chem.delete();
        assertFalse(chem.verify());

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
     * @throws SQLException
     */
    @Test
    public void testGetAllCompounds() throws SQLException {
        ArrayList<CompoundToElementDTO> listTester = new ArrayList<>();
        CompoundDataGateway gw = new CompoundDataGateway(conn, 1);
        CompoundToElementDTO compOne = new CompoundToElementDTO(1, 1234);
        listTester.add(compOne);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(1, 5678);
        listTester.add(compTwo);
        CompoundToElementDTO compThree = new CompoundToElementDTO(2, 1234);
        listTester.add(compThree);
        ArrayList<CompoundToElementDTO> listMethod = gw.getAllCompounds();

        ArrayList<Long> testerIDs = new ArrayList<>();
        testerIDs.add((listTester.get(0)).compoundID);
        testerIDs.add((listTester.get(1)).compoundID);
        testerIDs.add((listTester.get(2)).compoundID);
        ArrayList<Long> methodIDs = new ArrayList<>();
        methodIDs.add((listMethod.get(0)).compoundID);
        methodIDs.add((listMethod.get(1)).compoundID);
        methodIDs.add((listMethod.get(2)).compoundID);

        Assertions.assertEquals(testerIDs, methodIDs);
    }

    /**
     * Tests that getting compounds by element id will only return the correct compounds
     * @throws SQLException
     */
    @Test
    public void testGetCompoundsByElementID() throws SQLException {
        ArrayList<CompoundToElementDTO> listTester = new ArrayList<>();
        CompoundDataGateway gw = new CompoundDataGateway(conn, 1);
        CompoundToElementDTO compOne = new CompoundToElementDTO(1, 1234);
        listTester.add(compOne);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(2, 1234);
        listTester.add(compTwo);
        ArrayList<CompoundToElementDTO> listMethod = gw.getCompoundsContaining(1234);

        ArrayList<Long> testerIDs = new ArrayList<>();
        testerIDs.add((listTester.get(0)).compoundID);
        testerIDs.add((listTester.get(1)).compoundID);
        ArrayList<Long> methodIDs = new ArrayList<>();
        methodIDs.add((listMethod.get(0)).compoundID);
        methodIDs.add((listMethod.get(1)).compoundID);

        Assertions.assertEquals(testerIDs, methodIDs);
    }

    /**
     * Tests that getting compound elements will return the correct compound elements
     * @throws SQLException
     */
    @Test
    public void testGetElementsInCompound() throws SQLException {
        ArrayList<CompoundToElementDTO> listTester = new ArrayList<>();
        CompoundDataGateway gw = new CompoundDataGateway(conn, 1);
        CompoundToElementDTO compOne = new CompoundToElementDTO(1, 1234);
        listTester.add(compOne);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(1, 5678);
        listTester.add(compTwo);
        ArrayList<CompoundToElementDTO> listMethod = gw.getElementsInCompound(1);

        ArrayList<Long> testerIDs = new ArrayList<>();
        testerIDs.add((listTester.get(0)).elementID);
        testerIDs.add((listTester.get(1)).elementID);
        ArrayList<Long> methodIDs = new ArrayList<>();
        methodIDs.add((listMethod.get(0)).elementID);
        methodIDs.add((listMethod.get(1)).elementID);

        Assertions.assertEquals(testerIDs, methodIDs);
    }
}