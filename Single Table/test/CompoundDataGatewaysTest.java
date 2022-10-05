package test;


import org.junit.Test;
import src.CompoundDataGateway;
import src.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class CompoundDataGatewaysTest {
    /**
     * Tests that getting all compounds will retrieve all the compounds
     * @throws SQLException
     */
    @Test
    public void testGetAllCompounds() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        Compound compOne = new Compound(12345, 9999);
        Compound compTwo = new Compound(5678, 9999);
        ArrayList<Compound> methodList = getAllCompounds();
        ArrayList<Compound> normalList;
        normalList.append(compOne);
        normalList.append(compTwo);

        assertEquals(normalList, methodList);
    }

    /**
     * Tests that getting compounds by element id will only return the correct compounds
     * @throws SQLException
     */
    @Test
    public void testGetCompoundsByElementID() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        Compound compOne = new Compound(12345, 9999);
        Compound compTwo = new Compound(5678, 9999);
        ArrayList<Compound> methodList = getCompoundsByElementID(9999);
        ArrayList<Compound> normalList;
        normalList.append(compOne);
        normalList.append(compTwo);

        assertEquals(normalList, methodList);
    }
}