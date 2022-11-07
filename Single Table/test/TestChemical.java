import datasource.ChemicalDataGateway;
import datasource.Gateway;
import datasource.dto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
                "dissolvedBy, type) VALUES (4, 'TestChemical', 3, 3, 3, 3, 3, 'Chemical')");
        stmt.executeUpdate("INSERT INTO Chemical (id, name, atomicNumber, atomicMass, baseSolute, acidSolute," +
                "dissolvedBy, type) VALUES (5, 'TestCompound', 0, 0, 0, 0, 0, 'Compound')");
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
    public void testInitByName() throws SQLException {
        assertNotNull(conn);

        ChemicalDataGateway chem = new ChemicalDataGateway(conn, "TestMetal");
        assertEquals("1", chem.getId());
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
        assertEquals(chem.getName(), trueName);
        assertEquals(chem.getAtomicNumber(), trueNumber);
        assertEquals(chem.getAtomicMass(), trueMass);
        assertEquals(chem.getAcidSolute(), trueASolute);
        assertEquals(chem.getBaseSolute(), trueBSolute);
        assertEquals(chem.getDissolvedBy(), trueDissolved);
        assertEquals(chem.getType(), trueType);

        // Test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, trueName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);
            assertEquals(rs.getInt("atomicNumber"), trueNumber);
            assertEquals(rs.getDouble("atomicMass"), trueMass);
            assertEquals(rs.getLong("acidSolute"), trueASolute);
            assertEquals(rs.getLong("baseSolute"), trueBSolute);
            assertEquals(rs.getLong("dissolvedBy"), trueDissolved);
            assertEquals(rs.getString("type"), trueType);
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
     * @throws SQLException - for problems that may occur in the database
     */
    @Test
    public void testGetMetalsDissolvedBy() throws SQLException {
        ArrayList<Long> listTester = new ArrayList<>();
        ChemicalDataGateway metalGW = new ChemicalDataGateway(conn, 1);
        MetalDTO metal = new MetalDTO(1, metalGW.getName(), metalGW.getAtomicNumber(),
                metalGW.getAtomicMass(), metalGW.getDissolvedBy());
        listTester.add(metal.id);

        ArrayList<MetalDTO> listMethod = metalGW.getMetalsDissolvedBy(3);
        ArrayList<Long> listMethodIDs = new ArrayList<>();
        listMethodIDs.add(listMethod.get(0).id);

        Assertions.assertEquals(listTester, listMethodIDs);
    }

    /**
     * Tests the use of Table Data Gateway Functions based on getting
     * Chemicals of a Certain Type
     * @throws SQLException - for problems that may occur in the database
     */
    @Test
    public void testGetAllOfAType() throws SQLException {
        //GetAllMetals
        ArrayList<MetalDTO> listMetalTester = new ArrayList<>();
        ChemicalDataGateway metalGW = new ChemicalDataGateway(conn, 1);
        MetalDTO metal = new MetalDTO(1, metalGW.getName(), metalGW.getAtomicNumber(), metalGW.getAtomicMass(),
                metalGW.getDissolvedBy());
        ArrayList<MetalDTO> listMetalMethod = metalGW.getAllMetals();
        listMetalTester.add(metal);

        long testerID = (listMetalTester.get(0)).id;
        long methodID = (listMetalMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listMetalTester.clear();
        listMetalMethod.clear();

        //GetAllBases
        ArrayList<BaseDTO> listBaseTester = new ArrayList<>();
        ArrayList<BaseDTO> listBaseMethod = new ArrayList<>();
        ChemicalDataGateway baseGW = new ChemicalDataGateway(conn, 2);
        BaseDTO base = new BaseDTO(2, baseGW.getName(), baseGW.getBaseSolute());
        listBaseTester.add(base);
        listBaseMethod = baseGW.getAllBases();

        testerID = (listBaseTester.get(0)).id;
        methodID = (listBaseMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listBaseTester.clear();
        listBaseMethod.clear();

        //GetAllAcids
        ArrayList<AcidDTO> listAcidTester = new ArrayList<>();
        ArrayList<AcidDTO> listAcidMethod = new ArrayList<>();
        ChemicalDataGateway acidGW = new ChemicalDataGateway(conn, 3);
        AcidDTO acid = new AcidDTO(3, acidGW.getName(), acidGW.getAcidSolute());
        listAcidTester.add(acid);
        listAcidMethod = acidGW.getAllAcids();

        testerID = (listAcidTester.get(0)).id;
        methodID = (listAcidMethod.get(0)).id;

        Assertions.assertEquals(testerID, methodID);
        listAcidTester.clear();
        listAcidMethod.clear();

        //GetAllCompounds
        ChemicalDataGateway gw = new ChemicalDataGateway(conn, 5);
        CompoundDTO comp = new CompoundDTO(5, "TestCompound");
        ArrayList<CompoundDTO> listCompMethod = gw.getAllCompounds();

        testerID = comp.id;
        methodID = listCompMethod.get(0).id;

        Assertions.assertEquals(testerID, methodID);

        //GetAllElements
        ArrayList<ElementDTO> listElementMethod = new ArrayList<>();
        ChemicalDataGateway elemGW = new ChemicalDataGateway(conn, 4);
        ElementDTO elem = new ElementDTO(4, elemGW.getName(), elemGW.getAtomicNumber(), elemGW.getAtomicMass());
        listElementMethod = elemGW.getAllElementIDs();

        ArrayList<Long> testerIDs = new ArrayList<>();
        testerIDs.add(metal.id);
        testerIDs.add(base.id);
        testerIDs.add(acid.id);
        testerIDs.add(elem.id);
        testerIDs.add(comp.id);

        ArrayList<Long> methodIDs = new ArrayList<>();
        methodIDs.add((listElementMethod.get(0)).id);
        methodIDs.add((listElementMethod.get(1)).id);
        methodIDs.add((listElementMethod.get(2)).id);
        methodIDs.add((listElementMethod.get(3)).id);
        methodIDs.add((listElementMethod.get(4)).id);

        Assertions.assertEquals(testerIDs, methodIDs);
    }

    /**
     * Ensures that getters and setters are working properly and are changing
     * both within the database and on our end.
     */
    @Test
    public void testUpdaters() throws SQLException {
        assertNotNull(conn);

        //UpdateName
        String trueName = "TestChemical";
        String tempName = "TestMetal";

        ChemicalDataGateway myChemical = new ChemicalDataGateway(conn, 4);

        // test that the value is set properly for the object
        assertEquals(myChemical.getName(), trueName);

        // test that the value exists in the database
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = 4");
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals(rs.getString("name"), trueName);
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
        String trueType = "Chemical";
        String tempType = "Metal";

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