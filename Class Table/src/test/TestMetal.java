package test;

import datasource.ElementDataGateway;
import datasource.MetalDataGateway;
import datasource.Gateway;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestMetal {

    private final Connection conn = Gateway.setUpConnection();

    /**
     * When initialized by id, a MetalDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";
        int trueNumber = 1;
        double trueMass = 5.0;
        long trueDissolve = 75;


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

            statement = conn.prepareCall("INSERT INTO Metal (`id`, `dissolvedBy`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setLong(2, trueDissolve);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        MetalDataGateway metal = new MetalDataGateway(trueId);
        // Does it correspond to the right row?
        assertEquals(metal.getName(), trueName);
        assertEquals(metal.getAtomicNumber(), trueNumber);
        assertTrue(metal.getAtomicMass() == trueMass);
    }

    /**
     * Checks that when a MetalDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";
        int trueNumber = 1;
        double trueMass = 5.0;
        long trueDissolve = 75;

        MetalDataGateway metal = new MetalDataGateway(trueName, trueNumber, trueMass, trueDissolve);
        // Test that the value is set properly for the Object
        assertEquals(metal.getName(), trueName);
        assertEquals(metal.getAtomicNumber(), trueNumber);
        assertTrue(metal.getAtomicMass() == trueMass);
        assertEquals(metal.getDissolvedBy(), trueDissolve);


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
            assertTrue(rs.getInt("atomicMass") == trueMass);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);

            statement = conn.prepareCall("SELECT * from Metal WHERE dissolvedBy = ?");
            statement.setLong(1, trueDissolve);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("dissolvedBy"), trueDissolve);
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Ensures that an MetalDataGateway is removed from the database properly
     */
    @Test
    public void testDelete() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";
        int trueNumber = 1;
        double trueMass = 5.0;
        long trueDissolve = 75;

        // Create rows
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

            statement = conn.prepareCall("INSERT INTO Metal (`id`, `dissolvedBy`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setLong(2, trueDissolve);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        MetalDataGateway metal = new MetalDataGateway(trueId);

        // Does the deleted boolean change?
        assertTrue(metal.verify());
        metal.delete();
        assertFalse(metal.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Acid WHERE id = ?");
            statement.setLong(1, trueId);
            ResultSet rs = statement.executeQuery();
            assertFalse(rs.next());

            // Must be removed from all 3 tables
            statement = conn.prepareCall("SELECT * FROM Element WHERE id = ?");
            statement.setLong(1, trueId);
            rs = statement.executeQuery();
            assertFalse(rs.next());

            statement = conn.prepareCall("SELECT * FROM Metal WHERE id = ?");
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
        int tempNumber = 6;
        double trueMass = 10.0;
        double tempMass = 12.0;
        long trueDissolve = 55;


        MetalDataGateway myMetal = new MetalDataGateway(trueName, trueNumber, trueMass, trueDissolve);

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

            statement = conn.prepareCall("SELECT * from Metal WHERE dissolvedBy = ?");
            statement.setLong(1, trueDissolve);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("dissolvedBy"), trueDissolve);
        } catch (SQLException e) {
            fail();
        }

        // update to new values
        myMetal.updateAtomicNumber(tempNumber);
        myMetal.updateAtomicMass((tempMass));
        myMetal.updateDissolvedBy(trueDissolve);

        // test that the values have changed in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);

            statement = conn.prepareCall("SELECT * from Element WHERE atomicMass = ?");
            statement.setDouble(1, tempMass);
            rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getDouble("atomicMass") == tempMass);
            assertEquals(rs.getInt("atomicNumber"), tempNumber);

        } catch (SQLException e) {
            fail();
        }

        // test that the value has changed on our end
        assertNotEquals(trueMass, myMetal.getAtomicMass());
        assertNotEquals(trueNumber, myMetal.getAtomicNumber());

    }
}

