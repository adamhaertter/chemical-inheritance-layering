package test;

import datasource.AcidDataGateway;
import datasource.Gateway;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestAcid {

    private Connection conn = Gateway.setUpConnection();

    /**
     * When initialized by id, an AcidDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";
        long trueSolute = 1;

        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);

            statement = conn.prepareCall("INSERT INTO Acid (`id`, `solute`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setLong(2, trueSolute);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        AcidDataGateway acid = new AcidDataGateway(trueId);
        // Does it correspond to the right row?
        assertTrue(acid.getName().equals(trueName));
        assertEquals(acid.getSolute(), trueSolute);
    }

    /**
     * Checks that when an AcidDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() {
        assertNotNull(conn);

        String trueName = "Ex";
        long trueSolute = 1;

        AcidDataGateway acid = new AcidDataGateway(trueName, trueSolute);
        // Test that the value is set properly for the Object
        assertTrue(acid.getName().equals(trueName));

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));

            statement = conn.prepareCall("SELECT * from Acid WHERE solute = ?");
            statement.setLong(1, trueSolute);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("solute"), trueSolute);
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
        long trueSolute = 1;

        // Create rows in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (`id`, `name`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);

            statement = conn.prepareCall("INSERT INTO Acid (`id`, `solute`) VALUES (?, ?)");
            statement.setLong(1, trueId);
            statement.setLong(2, trueSolute);
            result = statement.executeUpdate();
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        AcidDataGateway acid = new AcidDataGateway(trueId);

        // Does the deleted boolean change?
        assertTrue(acid.verify());
        acid.delete();
        assertFalse(acid.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Acid WHERE id = ?");
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

    /**
     * ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdateSolute() {

        assertNotNull(conn);

        String trueName = "Tester";
        long trueSolute = 1;
        long tempSolute = 3;

        AcidDataGateway myAcid = new AcidDataGateway(trueName, trueSolute);
        assertTrue(myAcid.getName().equals(trueName));
        assertTrue(myAcid.getSolute() == trueSolute);

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));

            statement = conn.prepareCall("SELECT * from Acid WHERE solute = ?");
            statement.setLong(1, trueSolute);
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("solute"), trueSolute);
        } catch (SQLException e) {
            fail();
        }

        // set solute to new value
        myAcid.updateSolute(tempSolute);

        // test that the value has changed in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Acid WHERE solute = ?");
            statement.setLong(1, tempSolute);
            ResultSet rs = statement.executeQuery();
            rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("solute"), tempSolute);

        } catch (SQLException e) {
            fail();
        }

        // test that the value has changed on our end
        assertNotEquals(trueSolute, myAcid.getSolute());

    }

}
