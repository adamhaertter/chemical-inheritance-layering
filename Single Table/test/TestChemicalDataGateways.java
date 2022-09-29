package test;


import org.junit.Test;
import src.ChemicalDataGateways;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static src.ChemicalDataGateways.m_dbConn;

public class TestChemicalDataGateways {
    String[] dissolved = new String[]{"HCl", "H2O"};
    @Test
    public void testPersistsWithID() throws SQLException {
        Statement stmt = m_dbConn.createStatement();

        ChemicalDataGateways Gateway = new ChemicalDataGateways(1234567);
        Gateway.persists();
        String statement = new String("SELECT * FROM project-1-single-table WHERE id=1234567");
        stmt.execute(statement);

        ResultSet getID = stmt.getResultSet();
        assertEquals(1234567, getID.getLong("id"));
    }

    @Test
    public void testPersistsWithEverything() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        ChemicalDataGateways Gateway = new ChemicalDataGateways("Iron", 26, 55.85,
                0, 0, dissolved, 5678);
        Gateway.persists();
        String statement = new String("SELECT * FROM project-1-single-table WHERE name=\"Iron\"");
        stmt.execute(statement);

        ResultSet getInfo = stmt.getResultSet();
        assertEquals("Iron", getInfo.getString("name"));
        assertEquals(26, getInfo.getInt("atomicNumber"));
        assertEquals(55.85, getInfo.getDouble("atomicMass"));
        assertEquals(0, getInfo.getInt("baseSolute"));
        assertEquals(0, getInfo.getInt("acidSolute"));
        assertEquals(dissolved[0], getInfo.getString("dissolved"));
        assertEquals(5678, getInfo.getLong("dissolvedBy"));
    }

    @Test
    public void testGetAllCompounds() throws SQLException {
        Statement stmt = m_dbConn.createStatement();


    }

    @Test
    public void testGetMetalsDissolvedBy() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        long[] IDList = new long[];
        String statement = new String("SELECT * FROM project-1-single-table WHERE dissolvedBy=5678");
        stmt.execute(statement);

        ResultSet getInfo = stmt.getResultSet();
        for(int i = 0; getInfo.next(); i++) {
            IDList[i] = getInfo.getLong("id");
        }
    }
}