package test;

import datasource.ChemicalDataGateway;
import datasource.Gateway;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.testng.Assert.assertNotEquals;


public class TestChemical {

    private Connection conn = Gateway.setUpConnection();

    /**
     * When initialized by id, a ChemicalDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";

        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        ChemicalDataGateway chem = new ChemicalDataGateway(trueId);
        // Does it correspond to the right row?
        assertTrue(chem.getName().equals(trueName));
    }

    /**
     * Checks that when a ChemicalDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";
        ChemicalDataGateway chem = new ChemicalDataGateway(trueName);
        // Test that the value is set properly for the Object
        assertTrue(chem.getName().equals(trueName));

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that a ChemicalDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";

        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        ChemicalDataGateway chem = new ChemicalDataGateway(trueId);

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
     * ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdateName() {
        assertNotNull(conn);

        String trueName = "MyTestName";
        String tempName = "MyTempName";

        ChemicalDataGateway myChemical = new ChemicalDataGateway(trueName);

        // test that the value is set properly for the object
        assertTrue(myChemical.getName().equals(trueName));

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
        } catch (SQLException e) {
            fail();
        }

        // set name to new name
        myChemical.updateName(tempName);

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, tempName);
            ResultSet rs = statement.executeQuery();
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), tempName);

        } catch (SQLException e) {
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueName, myChemical.getName());
    }

}
