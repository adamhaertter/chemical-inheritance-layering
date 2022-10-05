package test;


import org.junit.Test;
import src.ChemicalDataGateway;
import src.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class ChemicalDataGatewaysTest {
    String[] dissolved = new String[]{"HCl", "H2O"};

    /**
     * Tests when it initializes with identification
     */
    @Test
    public void testInitializationWithID() {
        ChemicalDataGateways Gateway = new ChemicalDataGateways(1234567);
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
    @Test
    public void testInitializationWithEverything() {
        //Statement stmt = m_dbConn.createStatement();
        ChemicalDataGateways Gateway = new ChemicalDataGateways("Iron", 26, 55.85,
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
        assertEquals(dissolved, Gateway.getDissolved());
        assertEquals(5678, Gateway.getDissolvedBy());
    }

    /**
     * Tests that deleting items works
     */
    @Test
    public void testDelete() {
        Statement stmt = m_dbConn.createStatement();
        ChemicalDataGateways Gateway = new ChemicalDataGateways(1234567);
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
    @Test
    public void testGetDissolvedBy() throws SQLException {
        ChemicalDataGateways gatewayOne = new ChemicalDataGateways(1234);
        gatewayOne.setDissolvedBy(1010);
        ChemicalDataGateways gatewayTwo = new ChemicalDataGateways(5678);
        gatewayTwo.setDissolvedBy(1010);
        ChemicalDataGateways gatewayThree = new ChemicalDataGateways(9999);
        gatewayThree.setDissolvedBy(1010);

        long[] IDListMethod = getMetalsDissolvedBy(1010);
        long[] IDList = new long[1234, 5678, 9999];

        assertEquals(IDList, IDListMethod);
    }
}