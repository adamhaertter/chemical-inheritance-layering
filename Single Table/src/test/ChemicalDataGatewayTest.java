import config.ProjectConfig;
import datasource.ChemicalDataGateway;
import datasource.Gateway;
import dto.ChemicalDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ChemicalDataGatewayTest {
    private final Connection conn = Gateway.setUpConnection();

    @BeforeEach
    public void setUp() throws SQLException {
        conn.setAutoCommit(false);

        // Insert Test Data
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type)" + " VALUES (1, 'TestMetal', 1, 0, 0, 0, 3, 'Metal')");
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type) VALUES (2, 'TestBase', 2, 0, 0, 0, 0, 'Base')");
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type) VALUES (3, 'TestAcid', 3, 0, 0, 0, 0, 'Acid')");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Tests when it initializes with all the information
     */
    @Test
    public void testInitializationWithEverything() {
        ChemicalDataGateway Gateway = new ChemicalDataGateway(conn, "Iron", 26, 55.85,
                0, 0, 5678, "Metal");

        Assertions.assertEquals("Iron", Gateway.getName());
        Assertions.assertEquals(26, Gateway.getAtomicNumber());
        Assertions.assertEquals(55.85, Gateway.getAtomicMass());
        Assertions.assertEquals(0, Gateway.getBaseSolute());
        Assertions.assertEquals(0, Gateway.getAcidSolute());
        Assertions.assertEquals(5678, Gateway.getDissolvedBy());
        Assertions.assertEquals("Metal", Gateway.getType());
    }

    /**
     * Tests that deleting items works
     */
    @Test
    public void testDelete() throws SQLException {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";

        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (id, name, atomicNumber," +
                    "atomicMass, baseSolute, acidSolute, dissolvedBy, type)" +
                    "VALUES (?, ?, 0, 0, 0, 0, 0, 'Test')");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        ChemicalDataGateway chem = new ChemicalDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(chem.verify());
        chem.delete();
        assertFalse(chem.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
            statement.setLong(1, trueId);
            ResultSet rs = statement.executeQuery();
            assertFalse(rs.next());
        } catch (SQLException e) {
            fail();
        }

    }

    /**
     * Tests the getDissolvedBy function and setDissolvedBy function
     * to ensure it is getting the correct array values
     * @throws SQLException
     */
    @Test
    public void testGetMetalsDissolvedBy() throws SQLException {
        ArrayList<Long> listTester = new ArrayList<>();
        ChemicalDataGateway metalGW = new ChemicalDataGateway(conn, 1);
        ChemicalDTO metal = new ChemicalDTO(1, metalGW.getName(), metalGW.getAtomicNumber(), metalGW.getAtomicMass(),
                metalGW.getBaseSolute(), metalGW.getAcidSolute(), metalGW.getDissolvedBy(),
                metalGW.getType());
        listTester.add(metal.id);

        ArrayList<ChemicalDTO> listMethod = metalGW.getMetalsDissolvedBy(3);
        ArrayList<Long> listMethodIDs = new ArrayList<>();
        listMethodIDs.add(listMethod.get(0).id);

        Assertions.assertEquals(listTester, listMethodIDs);
    }

    @Test
    public void testGetAllOfAType() throws SQLException {
        ArrayList<ChemicalDTO> listTester = new ArrayList<>();
        ChemicalDataGateway acidGW = new ChemicalDataGateway(conn, 3);
        ChemicalDTO acid = new ChemicalDTO(3, acidGW.getName(), acidGW.getAtomicNumber(), acidGW.getAtomicMass(),
                                        acidGW.getBaseSolute(), acidGW.getAcidSolute(), acidGW.getDissolvedBy(),
                                        acidGW.getType());
        listTester.add(acid);
        ArrayList<ChemicalDTO> listMethod = acidGW.getAllAcids();

        long testerID = (listTester.get(0)).id;
        long methodID = (listMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listTester.clear();
        listMethod.clear();

        ChemicalDataGateway metalGW = new ChemicalDataGateway(conn, 1);
        ChemicalDTO metal = new ChemicalDTO(1, metalGW.getName(), metalGW.getAtomicNumber(), metalGW.getAtomicMass(),
                metalGW.getBaseSolute(), metalGW.getAcidSolute(), metalGW.getDissolvedBy(),
                metalGW.getType());
        listTester.add(metal);
        listMethod = metalGW.getAllMetals();

        testerID = (listTester.get(0)).id;
        methodID = (listMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listTester.clear();
        listMethod.clear();

        ChemicalDataGateway baseGW = new ChemicalDataGateway(conn, 2);
        ChemicalDTO base = new ChemicalDTO(2, baseGW.getName(), baseGW.getAtomicNumber(), baseGW.getAtomicMass(),
                baseGW.getBaseSolute(), baseGW.getAcidSolute(), baseGW.getDissolvedBy(),
                baseGW.getType());
        listTester.add(base);
        listMethod = baseGW.getAllBases();

        testerID = (listTester.get(0)).id;
        methodID = (listMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
    }
}