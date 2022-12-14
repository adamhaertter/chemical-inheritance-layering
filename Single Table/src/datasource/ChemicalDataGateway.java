package datasource;

import datasource.dto.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Used as a row data gateway for Chemical Table
 */
public class ChemicalDataGateway extends Gateway {
    String name = "";
    int atomicNumber = 0;
    double atomicMass = 0.0;
    long baseSolute = 0;
    long acidSolute = 0;
    long dissolvedBy = 0;
    String type = "";

    /**
     * Constructor used to assign values to a list of variables
     * @param n - the name of the chemical
     * @param number - the atomic number of the chemical
     * @param mass - the atomic mass of the chemical
     * @param aSolute - the acid solute of the chemical
     * @param bSolute - the base solute of the chemical
     * @param dissBy - the acid that a chemical is dissolved by
     * @param type - Type of Chemical (i.e. Metal, Acid, etc.)
     */
    public ChemicalDataGateway(Connection conn, String n, int number, double mass, long aSolute,
                               long bSolute, long dissBy, String type) {
        super(conn);
        this.name = n;
        this.atomicNumber = number;
        this.atomicMass = mass;
        this.acidSolute = aSolute;
        this.baseSolute = bSolute;
        this.dissolvedBy = dissBy;
        this.type = type;
        deleted = false;

        // Create in DB
        try {
            String addEntry = "INSERT INTO Chemical" + "(name, atomicNumber, atomicMass, baseSolute," +
                    "acidSolute, dissolvedBy, type) VALUES ('" + name + "', '" +
                    atomicNumber + "', '" + atomicMass + "', '" + baseSolute + "', '" +
                    acidSolute + "', '" + dissolvedBy + "', '" + type + "')";
            PreparedStatement ps = conn.prepareStatement(addEntry, Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructor that uses the id to create a row data gateway
     *
     * @param id - the id for the chemical
     * @throws SQLException - for problems that may occur in the database
     */
    public ChemicalDataGateway(Connection conn, long id) {
        super(conn);
        this.id = id;
        deleted = false;

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.atomicNumber = rs.getInt("atomicNumber");
            this.atomicMass = rs.getDouble("atomicMass");
            this.baseSolute = rs.getLong("baseSolute");
            this.acidSolute = rs.getLong("acidSolute");
            this.dissolvedBy = rs.getLong("dissolvedBy");
            this.type = rs.getString("type");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.atomicNumber = -1;
                this.atomicMass = -1;
                this.baseSolute = -1;
                this.acidSolute = -1;
                this.dissolvedBy = -1;
                this.type = null;
                System.out.println("No chemical was found with the given id: " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructor that uses the name to create a row data gateway
     *
     * @param name - the name for the chemical
     * @throws SQLException - for problems that may occur in the database
     */
    public ChemicalDataGateway(Connection conn, String name) {
        super(conn);
        this.name = name;
        deleted = false;

        // Read from DB
        try {
            String addEntry = "INSERT INTO Chemical" + "(name, atomicNumber, atomicMass, baseSolute," +
                    "acidSolute, dissolvedBy, type) VALUES ('" + name + "', '" +
                    atomicNumber + "', '" + atomicMass + "', '" + baseSolute + "', '" +
                    acidSolute + "', '" + dissolvedBy + "', '" + type + "')";
            PreparedStatement ps = conn.prepareStatement(addEntry, Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Getters & Setters ----------------------------------------------------------------------
    /**
     * Getter for the id
     * @return id - the id of the chemical
     */
    public long getId() {
        if (!deleted) {
            return id;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return -1;
    }

    /**
     * Getter for the name
     * @return name - the name of the chemical
     */
    public String getName() {
        if (!deleted) {
            return name;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return null;
    }

    /**
     * Setter for the name
     * @param name - the name of the chemical
     */
    public void updateName(String name) {
        if (!deleted) {
            if (persist(this.id, name, this.atomicNumber, this.atomicMass, this.baseSolute, this.acidSolute,
                    this.dissolvedBy, this.type))
                this.name = name;
        } else {
            System.out.println("This metal has been deleted.");
        }
    }

    /**
     * Getter for the atomic number
     * @return atomicNumber - the atomic number of the chemical
     */
    public int getAtomicNumber() {
        if (!deleted) {
            return atomicNumber;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return -1;
    }

    /**
     * Setter for the atomic number
     * @param atomicNumber - the atomic number of the chemical
     */
    public void updateAtomicNumber(int atomicNumber) {
        if (!deleted) {
            if (persist(this.id, this.name, atomicNumber, this.atomicMass, this.baseSolute, this.acidSolute,
                    this.dissolvedBy, this.type))
                this.atomicNumber = atomicNumber;
        } else {
            System.out.println("This metal has been deleted.");
        }
    }

    /**
     * Getter for the atomic mass
     * @return atomicMass - the atomic mass of the chemical
     */
    public double getAtomicMass() {
        if (!deleted) {
            return atomicMass;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return -1;
    }

    /**
     * Setter for the atomic mass
     * @param atomicMass - the atomic mass of the chemical
     */
    public void updateAtomicMass(double atomicMass) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, atomicMass, this.baseSolute, this.acidSolute,
                    this.dissolvedBy, this.type))
                this.atomicMass = atomicMass;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
    }

    /**
     * Getter for the acid solute
     * @return acidSolute - the acid solute of the chemical
     */
    public long getAcidSolute() {
        if (!deleted) {
            return acidSolute;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return -1;
    }

    /**
     * Set acid solute
     * @param acidSolute - the acid solute of the chemical
     */
    public void updateAcidSolute(long acidSolute) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, this.atomicMass, this.baseSolute, acidSolute,
                    this.dissolvedBy, this.type))
                this.acidSolute = acidSolute;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
    }

    /**
     * Getter for the base solute
     * @return baseSolute - the base solute of the chemical
     */
    public long getBaseSolute() {
        if (!deleted) {
            return baseSolute;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return -1;
    }

    /**
     * Setter for the base solute
     * @param baseSolute - the base solute of the chemical
     */
    public void updateBaseSolute(long baseSolute) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, this.atomicMass, baseSolute, this.acidSolute,
                    this.dissolvedBy, this.type))
                this.baseSolute = baseSolute;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
    }

    /**
     * Getter for dissolvedBy
     * @return dissolvedBy - the acid that a chemical is dissolved by
     */
    public long getDissolvedBy() {
        if (!deleted) {
            return dissolvedBy;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return -1;
    }

    /**
     * Setter for dissolvedBy
     * @param dissolvedBy - the acid that a chemical is dissolved by
     */
    public void updateDissolvedBy(long dissolvedBy) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, this.atomicMass, this.baseSolute, this.acidSolute,
                    dissolvedBy, this.type))
                this.dissolvedBy = dissolvedBy;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
    }

    /**
     * Getter for dissolvedBy
     * @return type - the type of chemical, (i.e. Metal, Nonmetal, etc.)
     */
    public String getType() {
        if (!deleted) {
            return type;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
        return null;
    }

    /**
     * Setter for type
     * @param type - the type of chemical, (i.e. Metal, Nonmetal, etc.)
     */
    public void updateType(String type) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, this.atomicMass, this.baseSolute, this.acidSolute,
                    this.dissolvedBy, type))
                this.type = type;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
    }

    //Table Data Gateway

    /**
     * Getter for all the metals dissolved by a certain acid
     * @param acidID - the acid that a chemical is dissolved by
     * @return metalList - the list of metals dissolved by a specific acid
     * @throws SQLException - for problems that may occur in the database
     */
    public ArrayList<MetalDTO> getMetalsDissolvedBy(long acidID) throws SQLException {
        ArrayList<MetalDTO> metalList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE dissolvedBy='"
                + acidID + "' AND type='Metal'");
        ResultSet overallSet = statement.getResultSet();

        while(overallSet.next()) {
            MetalDTO metal = new MetalDTO(
                    overallSet.getLong("id"), overallSet.getString("name"),
                    overallSet.getInt("atomicNumber"), overallSet.getLong("atomicMass"),
                    acidID);
            metalList.add(metal);
        }

        return metalList;
    }

    /**
     * Getter for all acids
     *
     * @return acidList - list of acids that dissolve a specified element
     * @throws SQLException - for problems that may occur in the database
     */
    public ArrayList<AcidDTO> getAllAcids() throws SQLException {
        ArrayList<AcidDTO> acidList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE type='Acid'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            AcidDTO acid = new AcidDTO(
                    rs.getLong("id"), rs.getString("name"), rs.getLong("acidSolute"));
            acidList.add(acid);
        }

        return acidList;
    }

    /**
     * Getter for all metals
     *
     * @return metalList - list of acids that dissolve a specified element
     * @throws SQLException - for problems that may occur in the database
     */
    public ArrayList<MetalDTO> getAllMetals() throws SQLException {
        ArrayList<MetalDTO> metalList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE type='Metal'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            MetalDTO metal = new MetalDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getInt("atomicNumber"), rs.getInt("atomicMass"), 0);
            metalList.add(metal);
        }

        return metalList;
    }

    /**
     * Getter for all bases
     *
     * @return baseList - list of bases that dissolve a specified element
     * @throws SQLException - for problems that may occur in the database
     */
    public ArrayList<BaseDTO> getAllBases() throws SQLException {
        ArrayList<BaseDTO> baseList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE type='Base'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            BaseDTO chem = new BaseDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getDouble("baseSolute"));
            baseList.add(chem);
        }

        return baseList;
    }

    /**
     * Getter for all the compounds in the table
     * @return allCompoundsList - a list of all the compounds in the table
     * @throws SQLException - for problems that may occur in the database
     */
    public ArrayList<CompoundDTO> getAllCompounds() throws SQLException {
        ArrayList<CompoundDTO> compoundList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.execute("SELECT * FROM Chemical WHERE type='Compound'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            CompoundDTO compound = new CompoundDTO(rs.getLong("id"), rs.getString("name"));
            compoundList.add(compound);
        }

        return compoundList;
    }

    /**
     * Getter for all chemicals
     *
     * @return chemicalList - list of chemicals
     * @throws SQLException - for problems that may occur in the database
     */
    public ArrayList<ElementDTO> getAllElementIDs() throws SQLException {
        ArrayList<ElementDTO> elementList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            ElementDTO elem = new ElementDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getInt("atomicNumber"), rs.getInt("atomicMass"));
            elementList.add(elem);
        }

        return elementList;
    }

    /**
     * Returns a list of all element names in the Database
     * @return all Elements in the database, by name
     */
    public static ArrayList<String> getElements(Connection conn) {
        ArrayList<String> elements = new ArrayList<String>();
        try {
            CallableStatement st = conn.prepareCall("SELECT * FROM Chemical");
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            while(rs.next()){
                elements.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    /**
     * Returns a list of all element names between 2 atomic numbers
     * @return the specified Elements in the database, by name
     */
    public static ArrayList<String> getElementsBetween(Connection conn, int first, int last) {
        ArrayList<String> elements = new ArrayList<String>();
        try {
            CallableStatement st = conn.prepareCall("SELECT * FROM Chemical WHERE atomicNumber BETWEEN ? AND ?");
            st.setInt(1, first);
            st.setInt(2, last);
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            while(rs.next()){
                elements.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    /**
     * Takes all the elements currently in the object and pushes them to the DB
     *
     * @return true or false
     */
    public boolean persist(long identity, String n, int number, double mass, long bSolute, long aSolute,
                           long dissolved, String t) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Chemical SET name = '" + n + "', " +
                    "atomicNumber = '" + number + "', atomicMass = '" + mass + "', baseSolute = '"
                    + bSolute + "', acidSolute = '" + aSolute + "', dissolvedBy = '" + dissolved +
                    "', type = '" + t + "' WHERE id = '" + identity + "'");
            return true;
        } catch (SQLException ex) {

            return false;
        }
    }

    /**
     * Given a name, return the id that links all the subtables together. Queries the database based on name.
     *
     * @param name The unique name of the Chemical, stored in this table and no others by our implementation.
     * @return the id shared by all instances of this Chemical in any subtables.
     */
    public static long getIdByName(Connection conn, String name) {
        try{
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            long id = rs.getLong("id");
            return id;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * If the entry is deleted, prints a message. Returns whether it has been deleted.
     * @return Whether the entry exists still.
     */
    public boolean verify() {
        if(deleted) {
            System.out.println("Entry " + this.name + " has been deleted.");
        }
        return !deleted;
    }


    /**
     * Checks the validity of the information in the row.
     *
     * @return Whether the current columns for this row have valid values
     */
    protected boolean validate() {
        return this.id != -1 && this.name != null && this.atomicNumber != -1 && this.atomicMass != -1 &&
                this.baseSolute != -1 && this.acidSolute != -1 && this.dissolvedBy != -1 && this.type != null;
    }

}
