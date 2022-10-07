package test;


import org.junit.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

class CompoundDataGatewayTest {
    /**
     * Tests that getting all compounds will retrieve all the compounds
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetAllCompounds() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        Compound compOne = new Compound(12345, 9999);
        Compound compTwo = new Compound(5678, 9999);
        ArrayList<Compound> methodList = getAllCompounds();
        ArrayList<Compound> normalList = new ArrayList<Compound>();
        normalList.append(compOne);
        normalList.append(compTwo);

        assertEquals(normalList, methodList);
    }

    /**
     * Tests that getting compounds by element id will only return the correct compounds
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetCompoundsByElementID() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        Compound compOne = new Compound(12345, 9999);
        Compound compTwo = new Compound(5678, 9999);
        ArrayList<Compound> methodList = getCompoundsByElementID(9999);
        ArrayList<Compound> normalList = new ArrayList<Compound>();
        normalList.append(compOne);
        normalList.append(compTwo);

        assertEquals(normalList, methodList);
    }

    /**
     * Tests that getting compound elements will return the correct compound elements
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetCompoundElements() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        Compound compOne = new Compound(12345, 9999);
        Compound compTwo = new Compound(5678, 9999);
        ArrayList<Compound> methodList = getCompoundElements(9999);
        ArrayList<Compound> normalList = new ArrayList<Compound>();
        normalList.append(compOne);
        normalList.append(compTwo);

        assertEquals(normalList, methodList);
    }
}