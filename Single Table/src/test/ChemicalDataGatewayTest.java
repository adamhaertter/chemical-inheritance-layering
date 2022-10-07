package test;

import config.ProjectConfig;
import datasource.ChemicalDataGateway;
import dto.ChemicalDTO;
import org.junit.jupiter.api.Assertions;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;

class ChemicalDataGatewayTest {
    String[] dissolved = new String[]{"HCl", "H2O"};
    private Connection conn;

    @org.junit.jupiter.api.Test
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        conn.setAutoCommit(false);

        // Insert Test Data
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT into Chemical VALUES (1, "TestMetal", 2)");
        stmt.executeUpdate("INSERT into Chemical VALUES (2, "TestBase", 1)");
        stmt.executeUpdate("INSERT into Chemical VALUES (3, "TestAcid", 2)");
    }

    @org.junit.jupiter.api.Test
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Tests when it initializes with all the information
     */
    @org.junit.jupiter.api.Test
    public void testInitializationWithEverything() {
        ChemicalDataGateway Gateway = new ChemicalDataGateway("Iron", 26, 55.85,
                0, 0, dissolved, 5678, "Metal");

        Assertions.assertEquals("Iron", Gateway.getName());
        Assertions.assertEquals(26, Gateway.getAtomicNumber());
        Assertions.assertEquals(55.85, Gateway.getAtomicMass());
        Assertions.assertEquals(0, Gateway.getBaseSolute());
        Assertions.assertEquals(0, Gateway.getAcidSolute());
        Assertions.assertEquals(dissolved, Gateway.getDissolves());
        Assertions.assertEquals(5678, Gateway.getDissolvedBy());
        Assertions.assertEquals("Metal", Gateway.getType());
    }

    /**
     * Tests that deleting items works
     */
    @org.junit.jupiter.api.Test
    public void testDelete() throws SQLException {
        Statement stmt = conn.createStatement();
        ChemicalDataGateway Gateway = new ChemicalDataGateway(1234567);
        Gateway.delete();
        String statement = new String("SELECT * FROM project-1-single-table WHERE id=1234567");
        stmt.execute(statement);
        ResultSet getInfo = stmt.getResultSet();

        assertNull(getInfo);
    }

    /**
     * Tests the getDissolvedBy function and setDissolvedBy function
     * to ensure it is getting the correct array values
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetMetalsDissolvedBy() throws SQLException {
        ChemicalDTO chemOne = new ChemicalDTO(1234);
        chemOne.setDissolvedBy(1010);
        chemOne.setType("Metal");
        ChemicalDataGateway gatewayTwo = new ChemicalDataGateway(5678);
        gatewayTwo.setDissolvedBy(1010);
        gatewayTwo.setType("Metal");
        ChemicalDataGateway gatewayThree = new ChemicalDataGateway(9999);
        gatewayThree.setDissolvedBy(1010);
        gatewayThree.setType("Metal");



        long[] IDListMethod = getMetalsDissolveBy(1010);
        long[] IDList = new long[1234, 5678, 9999];

        Assertions.assertEquals(IDList, IDListMethod);
    }

    @org.junit.jupiter.api.Test
    public void testGetAcidsThatDissolve() throws SQLException {
        ChemicalDataGateway gatewayOne = new ChemicalDataGateway(1234);
        gatewayOne.setDissolves(1010);
        gatewayOne.setType("Acid");
        ChemicalDataGateway gatewayTwo = new ChemicalDataGateway(5678);
        gatewayTwo.setDissolves(1010);
        gatewayTwo.setType("Acid");
        ChemicalDataGateway gatewayThree = new ChemicalDataGateway(9999);
        gatewayThree.setDissolves(1010);
        gatewayThree.setType("Acid");

        long[] IDListMethod = getAcidsThatDissolve(1010);
        long[] IDList = new long[1234, 5678, 9999];

        Assertions.assertEquals(IDList, IDListMethod);
    }

    @org.junit.jupiter.api.Test
    public void testGetAllOfAType() throws SQLException {
        ChemicalDataGateway acidGatewayOne = new ChemicalDataGateway(1234);
        acidGatewayOne.setType("Acid");
        ChemicalDataGateway acidGatewayTwo = new ChemicalDataGateway(5678);
        acidGatewayTwo.setType("Acid");
        ChemicalDataGateway acidGatewayThree = new ChemicalDataGateway(9999);
        acidGatewayThree.setType("Acid");

        long[] IDListMethod = getAllAcids();
        long[] IDList = new long[1234, 5678, 9999];

        Assertions.assertEquals(IDList, IDListMethod);

        ChemicalDataGateway metalGatewayOne = new ChemicalDataGateway(1234);
        metalGatewayOne.setType("Metal");
        ChemicalDataGateway metalGatewayTwo = new ChemicalDataGateway(5678);
        metalGatewayTwo.setType("Metal");
        ChemicalDataGateway metalGatewayThree = new ChemicalDataGateways\(9999);
        metalGatewayThree.setType("Metal");

        IDListMethod = getAllMetals();

        Assertions.assertEquals(IDList, IDListMethod);

        ChemicalDataGateway baseGatewayOne = new ChemicalDataGateway(1234);
        baseGatewayOne.setType("Base");
        ChemicalDataGateway baseGatewayTwo = new ChemicalDataGateway(5678);
        baseGatewayTwo.setType("Base");
        ChemicalDataGateway baseGatewayThree = new ChemicalDataGateway(9999);
        baseGatewayThree.setType("Base");

        IDListMethod = getAllAcids();

        Assertions.assertEquals(IDList, IDListMethod);
    }
}