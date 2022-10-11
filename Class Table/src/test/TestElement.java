package test;

import datasource.ElementDataGateway;
import datasource.Gateway;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestElement {

    private final Connection conn = Gateway.setUpConnection();

    /**
     * When initialized by id, an ElementDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";
        int trueNumber = 1;
        double trueMass = 5.0;


        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);

            statement = conn.prepareCall("INSERT INTO Element (`id`, `atomicNumber`, `atomicMass`) VALUES (?, ?, ?)");
            statement.setLong(1, trueId);
            statement.setInt(2, trueNumber);
            statement.setDouble(3, trueMass);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        ElementDataGateway elem = new ElementDataGateway(trueId);
        // Does it correspond to the right row?
        assertEquals(elem.getName(), trueName);
        assertEquals(elem.getAtomicNumber(), trueNumber);
        assertTrue(elem.getAtomicMass() == trueMass);
    }

    /**
     * Checks that when a ElementDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";
        int trueNumber = 1;
        double trueMass = 5;

        ElementDataGateway elem = new ElementDataGateway(trueName, trueNumber, trueMass);
        // Test that the value is set properly for the Object
        assertEquals(elem.getName(), trueName);
        assertEquals(elem.getAtomicNumber(), trueNumber);
        assertTrue(elem.getAtomicMass() == trueMass);


        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, trueMass);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that an AcidDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";
        int trueNumber = 1;
        double trueMass = 5.0;

        // Create rows in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);

            statement = conn.prepareCall("INSERT INTO Element (`id`, `atomicNumber`, `atomicMass`) VALUES (?, ?, ?)");
            statement.setLong(1, trueId);
            statement.setInt(2, trueNumber);
            statement.setDouble(3, trueMass);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        ElementDataGateway elem = new ElementDataGateway(trueId);

        // Does the deleted boolean change?
        assertTrue(elem.verify());
        elem.delete();
        assertFalse(elem.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Acid WHERE id = ?");
            statement.setLong(1, trueId);
            ResultSet rs = statement.executeQuery();
            assertFalse(rs.next());

            // Must be removed from both tables
            statement = conn.prepareCall("SELECT * FROM Element WHERE id = ?");
            statement.setLong(1, trueId);
            rs = statement.executeQuery();
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
    public void testUpdateMass() {
        assertNotNull(conn);

        String trueName = "TestElement";
        int trueNumber = 5;
        double trueMass = 10.0;
        double tempMass = 12.0;

        ElementDataGateway myElement = new ElementDataGateway(trueName, trueNumber, trueMass);

        // test that the values exist in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, trueMass);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
        } catch (SQLException e) {
            fail();
        }

        // set mass to new value
        myElement.updateAtomicMass(tempMass);

        // test that the value has changed in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, tempMass);
            ResultSet rs = statement.executeQuery();
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == tempMass);

        } catch (SQLException e) {
            fail();
        }

        assertNotEquals(trueMass, myElement.getAtomicMass());
    }
}
