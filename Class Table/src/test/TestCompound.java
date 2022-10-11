package test;

import datasource.CompoundDataGateway;
import datasource.Gateway;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestCompound {

    private Connection conn = Gateway.setUpConnection();

    /**
     * When initialized by id, a CompoundDataGateway should be able to retrieve a row that exists in the database with the same id
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

            statement = conn.prepareCall("INSERT INTO Compound (`id`) VALUES (?)");
            statement.setLong(1, trueId);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        CompoundDataGateway com = new CompoundDataGateway(conn, trueId);
        // Does it correspond to the right row?
        assertEquals(com.getName(), trueName);
    }

    /**
     * Checks that when a CompoundDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";

        CompoundDataGateway com = new CompoundDataGateway(conn, trueName);
        // Test that the value is set properly for the Object
        assertEquals(com.getName(), trueName);

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            long id = rs.getLong("id");

            statement = conn.prepareCall("SELECT * from Compound WHERE id = ?");
            statement.setLong(1, id);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("id"), id);
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that a CompoundDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";

        // Create rows in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);

            statement = conn.prepareCall("INSERT INTO Compound (`id`) VALUES (?)");
            statement.setLong(1, trueId);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        CompoundDataGateway com = new CompoundDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(com.verify());
        com.delete();
        assertFalse(com.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Compound WHERE id = ?");
            statement.setLong(1, trueId);
            ResultSet rs = statement.executeQuery();
            assertFalse(rs.next());

            // Must be removed from both tables
            statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
            statement.setLong(1, trueId);
            rs = statement.executeQuery();
            assertFalse(rs.next());
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void testUpdateName() {
        assertNotNull(conn);

        String trueName = "MyTestCompound";
        String tempName = "MyTempCompound";

        CompoundDataGateway myCompound = new CompoundDataGateway(conn, trueName);

        // test that the value is set properly for the object
        assertTrue(myCompound.getName().equals(trueName));

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Compound WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
        } catch (SQLException e) {
            fail();
        }

        // set name to new name
        myCompound.updateName(tempName);

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Compound WHERE name = ?");
            statement.setString(1, tempName);
            ResultSet rs = statement.executeQuery();
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), tempName);

        } catch (SQLException e) {
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueName, myCompound.getName());

    }

}
