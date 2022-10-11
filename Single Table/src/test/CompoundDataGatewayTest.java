package test;

import config.ProjectConfig;
import datasource.CompoundDataGateway;
import dto.CompoundToElementDTO;
//import org.junit.jupiter.api.*;
import org.junit.Test;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CompoundDataGatewayTest {
    private Connection conn;

    @Test
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        conn.setAutoCommit(false);

        // Insert Test Data
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO CompoundToElement (compoundId, elementId)" + "" +
                                "VALUES (1, 1234)");
        stmt.executeUpdate("INSERT INTO CompoundToElement (compoundId, elementId)" + "" +
                                "VALUES (1, 5678)");
    }

    @Test
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * Tests that getting all compounds will retrieve all the compounds
     * @throws SQLException
     */
    @Test
    public void testGetAllCompounds() throws SQLException {
        ArrayList<CompoundToElementDTO> listTester = new ArrayList<>();
        CompoundToElementDTO compOne = new CompoundToElementDTO(12345, 9999);
        listTester.add(compOne);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(12345, 8888);
        listTester.add(compTwo);
        CompoundDataGateway gw = new CompoundDataGateway(12345);
        ArrayList<CompoundToElementDTO> listMethod = gw.getAllCompounds();

        assertEquals(listTester, listMethod);
    }

    /**
     * Tests that getting compounds by element id will only return the correct compounds
     * @throws SQLException
     */
    @Test
    public void testGetCompoundsByElementID() throws SQLException {
        CompoundToElementDTO compOne = new CompoundToElementDTO(12345, 9999);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(5678, 9999);
        CompoundDataGateway gw = new CompoundDataGateway(12345);

        ArrayList<CompoundToElementDTO> listMethod = gw.getCompoundsContaining(9999);
        ArrayList<CompoundToElementDTO> listTester = new ArrayList<>();
        listTester.add(compOne);
        listTester.add(compTwo);

        assertEquals(listTester, listMethod);
    }

    /**
     * Tests that getting compound elements will return the correct compound elements
     * @throws SQLException
     */
    @Test
    public void testGetElementsInCompound() throws SQLException {
        CompoundToElementDTO compOne = new CompoundToElementDTO(12345, 9999);
        CompoundToElementDTO compTwo = new CompoundToElementDTO(12345,  8888);
        CompoundDataGateway gateway = new CompoundDataGateway(12345);

        ArrayList<CompoundToElementDTO> listMethod = gateway.getElementsInCompound(12345);
        ArrayList<CompoundToElementDTO> listTester = new ArrayList<>();
        listTester.add(compOne);
        listTester.add(compTwo);

        assertEquals(listTester, listMethod);
    }
}