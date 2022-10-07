package test;

import config.ProjectConfig;
import datasource.ChemicalDataGateway;
import org.junit.Assert;

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
        Statement stmnt = conn.createStatement();
        String insertTestBase = "INSERT into Chemical VALUES (1, 'TestBase', 2)";
        String insertTestSolute = "INSERT into Chemical VALUES (2, 'TestBase', 1)";
        stmnt.executeUpdate(insertTestSolute);
        stmnt.executeUpdate(insertTestBase);
    }

    @org.junit.jupiter.api.Test
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Tests when it initializes with identification
     */
    @org.junit.jupiter.api.Test
    public void testInitializationWithID() {
        ChemicalDataGateway Gateway = new ChemicalDataGateway(1234567);
        /*
        String statement = new String("SELECT * FROM project-1-single-table WHERE id=1234567");
        stmt.execute(statement);

        ResultSet getID = stmt.getResultSet();
        assertEquals(1234567, getID.getLong("id"));
         */
        assertEquals(1234567, Gateway.getID());
    }

    /**
     * Tests when it initializes with all the information
     */
    @org.junit.jupiter.api.Test
    public void testInitializationWithEverything() {
        //Statement stmt = m_dbConn.createStatement();
        ChemicalDataGateway Gateway = new ChemicalDataGateway("Iron", 26, 55.85,
                0, 0, dissolved, 5678);
        //String statement = new String("SELECT * FROM project-1-single-table WHERE name=\"Iron\"");
        //stmt.execute(statement);

        /*
        ResultSet getInfo = stmt.getResultSet();
        assertEquals("Iron", getInfo.getString("name"));
        assertEquals(26, getInfo.getInt("atomicNumber"));
        assertEquals(55.85, getInfo.getDouble("atomicMass"));
        assertEquals(0, getInfo.getInt("baseSolute"));
        assertEquals(0, getInfo.getInt("acidSolute"));
        assertEquals(dissolved[0], getInfo.getString("dissolved"));
        assertEquals(5678, getInfo.getLong("dissolvedBy"));
        */
        assertEquals("Iron", Gateway.getName());
        assertEquals(26, Gateway.getAtomicNumber());
        assertEquals(55.85, Gateway.getAtomicMass());
        assertEquals(0, Gateway.getBaseSolute());
        assertEquals(0, Gateway.getAcidSolute());
        assertEquals(dissolved, Gateway.getDissolves());
        assertEquals(5678, Gateway.getDissolvedBy());
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
        ChemicalDataGateway gatewayOne = new ChemicalDataGateway(1234);
        gatewayOne.setDissolvedBy(1010);
        gatewayOne.setType("Metal");
        ChemicalDataGateway gatewayTwo = new ChemicalDataGateway(5678);
        gatewayTwo.setDissolvedBy(1010);
        gatewayTwo.setType("Metal");
        ChemicalDataGateway gatewayThree = new ChemicalDataGateway(9999);
        gatewayThree.setDissolvedBy(1010);
        gatewayThree.setType("Metal");

        long[] IDListMethod = getMetalsDissolveBy(1010);
        long[] IDList = new long[1234, 5678, 9999];

        assertEquals(IDList, IDListMethod);
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

        assertEquals(IDList, IDListMethod);
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

        assertEquals(IDList, IDListMethod);

        ChemicalDataGateway metalGatewayOne = new ChemicalDataGateway(1234);
        metalGatewayOne.setType("Metal");
        ChemicalDataGateway metalGatewayTwo = new ChemicalDataGateway(5678);
        metalGatewayTwo.setType("Metal");
        ChemicalDataGateway metalGatewayThree = new ChemicalDataGateways\(9999);
        metalGatewayThree.setType("Metal");

        IDListMethod = getAllMetals();
        IDList = new long[1234, 5678, 9999];

        assertEquals(IDList, IDListMethod);

        ChemicalDataGateway baseGatewayOne = new ChemicalDataGateway(1234);
        baseGatewayOne.setType("Base");
        ChemicalDataGateway baseGatewayTwo = new ChemicalDataGateway(5678);
        baseGatewayTwo.setType("Base");
        ChemicalDataGateway baseGatewayThree = new ChemicalDataGateway(9999);
        baseGatewayThree.setType("Base");

        IDListMethod = getAllAcids();
        IDList = new long[1234, 5678, 9999];

        assertEquals(IDList, IDListMethod);
    }
}