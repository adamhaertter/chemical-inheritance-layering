import datasource.ChemicalDataGateway;
import datasource.Gateway;
import dto.ChemicalDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestChemical {
    private final Connection conn = Gateway.setUpConnection();

    @BeforeEach
    public void setUp() throws SQLException {
        conn.setAutoCommit(false);

        // Insert Test Data
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type)" + " VALUES (1, 'TestMetal', 1, 0, 0, 0, 3, 'Metal')");
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type) VALUES (2, 'TestBase', 2, 0, 0, 0, 0, 'Base')");
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type) VALUES (3, 'TestAcid', 3, 0, 0, 0, 0, 'Acid')");
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type) VALUES (4, 'TestMetal', 3, 3, 3, 3, 3, 'Metal')");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    /**
     * When initialized by id, a ChemicalDataGateway should be able to retrieve a row that exists in the database with the same id
     */
    @Test
    public void testInitById() throws SQLException {
        assertNotNull(conn);

        ChemicalDataGateway chem = new ChemicalDataGateway(conn, 1);
        // Does it correspond to the right row?
        assertTrue(chem.getName().equals("TestMetal"));
    }


    /**
     * Checks that when a ChemicalDataGateway is created with values, an equivalent row is created in the database
     */
    @Test
    public void testInitByVal() throws SQLException {
        assertNotNull(conn);

        String trueName = "Test";
        int trueNumber = 0;
        double trueMass = 0.0;
        long trueASolute = 0;
        long trueBSolute = 0;
        long trueDissolved = 0;
        String trueType = "Test";
        ChemicalDataGateway chem = new ChemicalDataGateway(conn, trueName, trueNumber, trueMass, trueASolute,
                                                        trueBSolute, trueDissolved, trueType);
        // Test that the value is set properly for the Object
        assertTrue(chem.getName().equals(trueName));
        assertEquals(chem.getAtomicNumber(), trueNumber);
        assertEquals(chem.getAtomicMass(), trueMass);
        assertEquals(chem.getAcidSolute(), trueASolute);
        assertEquals(chem.getBaseSolute(), trueBSolute);
        assertEquals(chem.getDissolvedBy(), trueDissolved);
        assertTrue(chem.getType().equals(trueType));

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
            assertEquals(rs.getDouble("atomicMass"), trueMass);
            assertEquals(rs.getLong("acidSolute"), trueASolute);
            assertEquals(rs.getLong("baseSolute"), trueBSolute);
            assertEquals(rs.getLong("dissolvedBy"), trueDissolved);
            assertTrue(rs.getString("type").equals(trueType));
        } catch (SQLException e) {
            fail();
        }
    }


    /**
     * Tests that deleting items works
     */
    @Test
    public void testDelete() throws SQLException {
        assertNotNull(conn);

        long trueId = 100L;
        String trueName = "Test";

        // Create row in table
        try {
            CallableStatement statement = conn.prepareCall("INSERT INTO Chemical (id, name, atomicNumber," +
                    "atomicMass, baseSolute, acidSolute, dissolvedBy, type)" +
                    "VALUES (?, ?, 0, 0, 0, 0, 0, 'Test')");
            statement.setLong(1, trueId);
            statement.setString(2, trueName);
            int result = statement.executeUpdate();
            // Was it inserted properly?
            assertNotEquals(0, result);
        } catch (SQLException e) {
            fail();
        }

        ChemicalDataGateway chem = new ChemicalDataGateway(conn, trueId);

        // Does the deleted boolean change?
        assertTrue(chem.verify());
        chem.delete();
        assertFalse(chem.verify());

        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM Chemical WHERE id = ?");
            statement.setLong(1, trueId);
            statement.executeQuery();
        } catch (SQLException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the getDissolvedBy function and setDissolvedBy function
     * to ensure it is getting the correct array values
     * @throws SQLException
     */
    @Test
    public void testGetMetalsDissolvedBy() throws SQLException {
        ArrayList<Long> listTester = new ArrayList<>();
        ChemicalDataGateway metalGW = new ChemicalDataGateway(conn, 1);
        ChemicalDTO metal = new ChemicalDTO(1, metalGW.getName(), metalGW.getAtomicNumber(), metalGW.getAtomicMass(),
                metalGW.getBaseSolute(), metalGW.getAcidSolute(), metalGW.getDissolvedBy(),
                metalGW.getType());
        listTester.add(metal.id);

        ArrayList<ChemicalDTO> listMethod = metalGW.getMetalsDissolvedBy(3);
        ArrayList<Long> listMethodIDs = new ArrayList<>();
        listMethodIDs.add(listMethod.get(0).id);

        Assertions.assertEquals(listTester, listMethodIDs);
    }

    @Test
    public void testGetAllOfAType() throws SQLException {
        ArrayList<ChemicalDTO> listTester = new ArrayList<>();
        ChemicalDataGateway acidGW = new ChemicalDataGateway(conn, 3);
        ChemicalDTO acid = new ChemicalDTO(3, acidGW.getName(), acidGW.getAtomicNumber(), acidGW.getAtomicMass(),
                                        acidGW.getBaseSolute(), acidGW.getAcidSolute(), acidGW.getDissolvedBy(),
                                        acidGW.getType());
        listTester.add(acid);
        ArrayList<ChemicalDTO> listMethod = acidGW.getAllAcids();

        long testerID = (listTester.get(0)).id;
        long methodID = (listMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listTester.clear();
        listMethod.clear();

        ChemicalDataGateway metalGW = new ChemicalDataGateway(conn, 1);
        ChemicalDTO metal = new ChemicalDTO(1, metalGW.getName(), metalGW.getAtomicNumber(), metalGW.getAtomicMass(),
                metalGW.getBaseSolute(), metalGW.getAcidSolute(), metalGW.getDissolvedBy(),
                metalGW.getType());
        listTester.add(metal);
        listMethod = metalGW.getAllMetals();

        testerID = (listTester.get(0)).id;
        methodID = (listMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listTester.clear();
        listMethod.clear();

        ChemicalDataGateway baseGW = new ChemicalDataGateway(conn, 2);
        ChemicalDTO base = new ChemicalDTO(2, baseGW.getName(), baseGW.getAtomicNumber(), baseGW.getAtomicMass(),
                baseGW.getBaseSolute(), baseGW.getAcidSolute(), baseGW.getDissolvedBy(),
                baseGW.getType());
        listTester.add(base);
        listMethod = baseGW.getAllBases();

        testerID = (listTester.get(0)).id;
        methodID = (listMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
    }

    /**
     * Ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdaters() throws SQLException {
        assertNotNull(conn);

        //UpdateName
        String trueName = "TestMetal";
        String tempName = "TestChemical";

        ChemicalDataGateway myChemical = new ChemicalDataGateway(conn, 4);

        // test that the value is set properly for the object
        assertTrue(myChemical.getName().equals(trueName));

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertTrue(rs.getString("name").equals(trueName));
        } catch (SQLException e) {
            fail();
        }

        // set name to new name
        myChemical.updateName(tempName);
        assertEquals(tempName, myChemical.getName());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), tempName);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueName, myChemical.getName());

        //UpdateAtomicNumber
        int trueNumber = 3;
        int tempNumber = 5;

        // test that the value is set properly for the object
        assertEquals(myChemical.getAtomicNumber(), trueNumber);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
        } catch (SQLException e) {
            fail();
        }

        // set Atomic Number to new Atomic Number
        myChemical.updateAtomicNumber(tempNumber);
        assertEquals(tempNumber, myChemical.getAtomicNumber());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getInt("atomicNumber"), tempNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueNumber, myChemical.getAtomicNumber());

        //UpdateAtomicMass----------------------------------------
        int trueMass = 3;
        int tempMass = 5;

        // test that the value is set properly for the object
        assertEquals(myChemical.getAtomicMass(), trueMass);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getDouble("atomicMass"), trueMass);
        } catch (SQLException e) {
            fail();
        }

        // set Mass to new Mass
        myChemical.updateAtomicMass(tempMass);
        assertEquals(tempMass, myChemical.getAtomicMass());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getDouble("atomicMass"), tempMass);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueMass, myChemical.getAtomicMass());

        //UpdateAcidSolute----------------------------------------
        long trueASolute = 3;
        long tempASolute = 5;

        // test that the value is set properly for the object
        assertEquals(myChemical.getAcidSolute(), trueASolute);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("acidSolute"), trueASolute);
        } catch (SQLException e) {
            fail();
        }

        // set Solute to new Solute
        myChemical.updateAcidSolute(tempASolute);
        assertEquals(tempASolute, myChemical.getAcidSolute());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("acidSolute"), tempASolute);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueASolute, myChemical.getAcidSolute());

        //UpdateBaseSolute----------------------------------------
        long trueBSolute = 3;
        long tempBSolute = 5;

        // test that the value is set properly for the object
        assertEquals(myChemical.getBaseSolute(), trueBSolute);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("baseSolute"), trueBSolute);
        } catch (SQLException e) {
            fail();
        }

        // set Solute to new Solute
        myChemical.updateBaseSolute(tempBSolute);
        assertEquals(tempBSolute, myChemical.getBaseSolute());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("baseSolute"), tempBSolute);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueBSolute, myChemical.getBaseSolute());

        //UpdateDissolvedBy----------------------------------------
        long trueDissolvedBy = 3;
        long tempDissolvedBy = 5;

        // test that the value is set properly for the object
        assertEquals(myChemical.getDissolvedBy(), trueDissolvedBy);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("dissolvedBy"), trueDissolvedBy);
        } catch (SQLException e) {
            fail();
        }

        // set Solute to new Solute
        myChemical.updateDissolvedBy(tempDissolvedBy);
        assertEquals(tempDissolvedBy, myChemical.getDissolvedBy());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getLong("dissolvedBy"), tempDissolvedBy);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueDissolvedBy, myChemical.getDissolvedBy());

        //UpdateDissolvedBy----------------------------------------
        String trueType = "Metal";
        String tempType = "Chemical";

        // test that the value is set properly for the object
        assertEquals(myChemical.getType(), trueType);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("type"), trueType);
        } catch (SQLException e) {
            fail();
        }

        // set Solute to new Solute
        myChemical.updateType(tempType);
        assertEquals(tempType, myChemical.getType());

        // test that the changes have been made in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("type"), tempType);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }

        // verify that the changes have been made on our end
        assertNotEquals(trueType, myChemical.getType());
    }
}