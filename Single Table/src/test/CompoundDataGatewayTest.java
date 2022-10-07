package test;


import datasource.CompoundDataGateway;
import dto.CompoundToElementDTO;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Assertions;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CompoundDataGatewayTest {
    private Connection conn;

    /**
     * Tests that getting all compounds will retrieve all the compounds
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetAllCompounds() throws SQLException {
        Statement stmt = conn.createStatement();
        CompoundToElementDTO compOne = new CompoundToElementDTO(12345, 9999);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(5678, 9999);
        ArrayList<CompoundToElementDTO> methodList = getAllCompounds();
        ArrayList<CompoundToElementDTO> normalList = new ArrayList<CompoundToElementDTO>();
        normalList.append(compOne);
        normalList.append(compTwo);

        Assertions.assertEquals(normalList, methodList);
    }

    /**
     * Tests that getting compounds by element id will only return the correct compounds
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetCompoundsByElementID() throws SQLException {
        Statement stmt = conn.createStatement();
        CompoundToElementDTO compOne = new CompoundToElementDTO(12345, 9999);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(5678, 9999);
        CompoundDataGateway gateway = new CompoundDataGateway(12345);

        ArrayList<CompoundToElementDTO> methodList = gateway.getCompoundsByElementID(9999);
        ArrayList<CompoundToElementDTO> normalList = new ArrayList<>();
        normalList.append(compOne);
        normalList.append(compTwo);

        Assertions.assertEquals(normalList, methodList);
    }

    /**
     * Tests that getting compound elements will return the correct compound elements
     * @throws SQLException
     */
    @org.junit.jupiter.api.Test
    public void testGetCompoundElements() throws SQLException {
        Statement stmt = conn.createStatement();
        CompoundToElementDTO compOne = new CompoundToElementDTO(12345, 9999);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(5678, 9999);
        CompoundDataGateway gateway = new CompoundDataGateway(12345);

        ArrayList<CompoundToElementDTO> methodList = gateway.getCompoundElements(9999);
        ArrayList<CompoundToElementDTO> normalList = new ArrayList<CompoundToElementDTO>();
        normalList.append(compOne);
        normalList.append(compTwo);

        Assertions.assertEquals(normalList, methodList);
    }
}