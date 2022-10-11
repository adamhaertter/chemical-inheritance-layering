package test;

import config.ProjectConfig;
import datasource.ChemicalDataGateway;
import dto.ChemicalDTO;
//import org.junit.jupiter.api.*;
import org.junit.Test;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

//import static org.junit.jupiter.api.Assertions.*;

class ChemicalDataGatewayTest {
    private Connection conn;

    @Test
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
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

    @Test
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Tests when it initializes with all the information
     */
    @org.junit.jupiter.api.Test
    public void testInitializationWithEverything() {
        ChemicalDataGateway Gateway = new ChemicalDataGateway("Iron", 26, 55.85, 0, 0, 5678, "Metal");

        assertEquals("Iron", Gateway.getName());
        assertEquals(26, Gateway.getAtomicNumber());
        assertEquals(55.85, Gateway.getAtomicMass());
        assertEquals(0, Gateway.getBaseSolute());
        assertEquals(0, Gateway.getAcidSolute());
        assertEquals(5678, Gateway.getDissolvedBy());
        assertEquals("Metal", Gateway.getType());
    }

    /**
     * Tests that deleting items works
     */
    @Test
    public void testDelete() throws SQLException {
        Statement stmt = conn.createStatement();
        ChemicalDataGateway Gateway = new ChemicalDataGateway(1);
        Gateway.delete();
        stmt.execute("SELECT * FROM Chemical WHERE id=1");
        ResultSet getInfo = stmt.getResultSet();

        assertNull(getInfo);
    }

    /**
     * Tests the getDissolvedBy function and setDissolvedBy function
     * to ensure it is getting the correct array values
     * @throws SQLException
     */
    @Test
    public void testGetMetalsDissolvedBy() throws SQLException {
        ArrayList<ChemicalDTO> listTester = new ArrayList<>();
        ChemicalDTO chemOne = new ChemicalDTO(1234, "Iron", 26, 55.845,
                0, 0, 1010, "Metal");
        ChemicalDataGateway cdg = new ChemicalDataGateway(1234);
        listTester.add(chemOne);
        ChemicalDTO chemTwo = new ChemicalDTO(5678, "Gold", 79, 196.96657,
                0, 0, 1010, "Metal");
        listTester.add(chemTwo);
        ChemicalDTO chemThree = new ChemicalDTO(9999, "Platinum", 78, 195.084,
                0, 0, 1010, "Metal");
        listTester.add(chemThree);

        ArrayList<ChemicalDTO> listMethod = cdg.getMetalsDissolvedBy(1010);

        assertEquals(listTester, listMethod);
    }

    @Test
    public void testGetAllOfAType() throws SQLException {
        ArrayList<ChemicalDTO> listTester = new ArrayList<>();
        ChemicalDTO acidOne = new ChemicalDTO(1234, "acidOne", 1,
                1, 0, 0, 0, "Acid");
        listTester.add(acidOne);
        ChemicalDataGateway acidGateway = new ChemicalDataGateway(acidOne.id);
        ChemicalDTO acidTwo = new ChemicalDTO(4567, "acidTwo", 1,
                1, 0, 0, 0, "Acid");
        listTester.add(acidTwo);
        ChemicalDTO acidThree = new ChemicalDTO(2685, "acidThree", 1,
                1, 0, 0, 0, "Acid");
        listTester.add(acidThree);

        ArrayList<ChemicalDTO> listMethod = acidGateway.getAllAcids();
        assertEquals(listTester, listMethod);
        listTester.clear();

        ChemicalDTO metalOne = new ChemicalDTO(1234, "metalOne", 1,
                1, 0, 0, 0, "Metal");
        ChemicalDataGateway metalGateway = new ChemicalDataGateway(metalOne.id);
        listTester.add(metalOne);
        ChemicalDTO metalTwo = new ChemicalDTO(4567, "metalTwo", 1,
                1, 0, 0, 0, "Metal");
        listTester.add(metalTwo);
        ChemicalDTO metalThree = new ChemicalDTO(2685, "metalThree", 1,
                1, 0, 0, 0, "Metal");
        listTester.add(metalThree);

        listMethod = metalGateway.getAllMetals();
        assertEquals(listTester, listMethod);
        listTester.clear();

        ChemicalDTO baseOne = new ChemicalDTO(1234, "baseOne", 1,
                1, 0, 0, 0, "Base");
        ChemicalDataGateway baseGateway = new ChemicalDataGateway(baseOne.id);
        listTester.add(baseOne);
        ChemicalDTO baseTwo = new ChemicalDTO(4567, "baseTwo", 1,
                1, 0, 0, 0, "Base");
        listTester.add(baseTwo);
        ChemicalDTO baseThree = new ChemicalDTO(2685, "baseThree", 1,
                1, 0, 0, 0, "Base");
        listTester.add(baseThree);

        listMethod = baseGateway.getAllBases();

        assertEquals(listTester, listMethod);
    }
}