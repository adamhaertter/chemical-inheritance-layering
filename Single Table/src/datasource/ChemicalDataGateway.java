package datasource;

import dto.ChemicalDTO;

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
     * Constructor that uses the id to create a row data gateway
     *
     * @param id - the id for the chemical
     * @throws SQLException
     */
    public ChemicalDataGateway(Connection conn, long id) throws SQLException {
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
     * Constructor used to assign values to a list of variables
     * @param n - the name of the chemical
     * @param number - the atomic number of the chemical
     * @param mass - the atomic mass of the chemical
     * @param bSolute - the base solute of the chemical
     * @param aSolute - the acid solute of the chemical
     * @param dissBy - the acid that a chemical is dissolved by
     * @param type - Type of Chemical (i.e. Metal, Acid, etc.)
     */
    public ChemicalDataGateway(Connection conn, String n, int number, double mass, long bSolute,
                               long aSolute, long dissBy, String type) {
        super(conn);
        this.name = n;
        this.atomicNumber = number;
        this.atomicMass = mass;
        this.baseSolute = bSolute;
        this.acidSolute = aSolute;
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



    //Getters & Setters ----------------------------------------------------------------------
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
     * @throws SQLException
     */
    public ArrayList<ChemicalDTO> getMetalsDissolvedBy(long acidID) throws SQLException {
        ArrayList<ChemicalDTO> metalList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE dissolvedBy='"
                + acidID + "' AND Type='Metal'");
        ResultSet overallSet = statement.getResultSet();

        while(overallSet.next()) {
            ChemicalDTO chem = new ChemicalDTO(
                    overallSet.getLong("id"), overallSet.getString("name"),
                    overallSet.getInt("atomicNumber"), overallSet.getLong("atomicMass"),
                    overallSet.getLong("baseSolute"), overallSet.getInt("acidSolute"),
                    acidID, overallSet.getString("type"));
            metalList.add(chem);
        }

        return metalList;
    }

    /**
     * Getter for all acids
     *
     * @return acidList - list of acids that dissolve a specified element
     * @throws SQLException
     */
    public ArrayList<ChemicalDTO> getAllAcids() throws SQLException {
        ArrayList<ChemicalDTO> acidList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE Type='Acid'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            ChemicalDTO chem = new ChemicalDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getInt("atomicNumber"), rs.getInt("atomicMass"),
                    rs.getLong("baseSolute"), rs.getLong("acidSolute"),
                    0, "Acid");
            acidList.add(chem);
        }

        return acidList;
    }

    /**
     * Getter for all metals
     *
     * @return metalList - list of acids that dissolve a specified element
     * @throws SQLException
     */
    public ArrayList<ChemicalDTO> getAllMetals() throws SQLException {
        ArrayList<ChemicalDTO> metalList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE Type='Metal'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            ChemicalDTO chem = new ChemicalDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getInt("atomicNumber"), rs.getInt("atomicMass"),
                    rs.getLong("baseSolute"), rs.getLong("acidSolute"),
                    0, "Metal");
            metalList.add(chem);
        }

        return metalList;
    }

    /**
     * Getter for all bases
     *
     * @return baseList - list of bases that dissolve a specified element
     * @throws SQLException
     */
    public ArrayList<ChemicalDTO> getAllBases() throws SQLException {
        ArrayList<ChemicalDTO> baseList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical WHERE Type='Base'");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            ChemicalDTO chem = new ChemicalDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getInt("atomicNumber"), rs.getInt("atomicMass"),
                    rs.getLong("baseSolute"), rs.getLong("acidSolute"),
                    rs.getLong("dissolvedBy"), "Acid");
            baseList.add(chem);
        }

        return baseList;
    }

    /**
     * Getter for all chemicals
     *
     * @return chemicalList - list of chemicals
     * @throws SQLException
     */
    public ArrayList<ChemicalDTO> getAllChemicals() throws SQLException {
        ArrayList<ChemicalDTO> chemicalList = new ArrayList<>();
        Statement statement = conn.createStatement();
        statement.executeQuery("SELECT * FROM Chemical");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            ChemicalDTO chem = new ChemicalDTO(
                    rs.getLong("id"), rs.getString("name"),
                    rs.getInt("atomicNumber"), rs.getInt("atomicMass"),
                    rs.getLong("baseSolute"), rs.getLong("acidSolute"),
                    rs.getInt("dissolvedBy"), "Acid");
            chemicalList.add(chem);
        }

        return chemicalList;
    }

    /**
     * Takes all the elements currently in the object and pushes them to the DB
     *
     * @return true or false
     */
    private boolean persist(long id, String name, int atomicNumber, double atomicMass, long baseSolute, long acidSolute,
                           long dissolvedBy, String type) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Chemical SET name = '" + name + "', " +
                    "atomicNumber = '" + atomicNumber + "', atomicMass = '" + atomicMass + "', baseSolute = '"
                    + baseSolute + "', acidSolute = '" + acidSolute + "', dissolvedBy = '" + dissolvedBy +
                    "', type = '" + type + "' WHERE id = '" + id + "'");
            return true;
        } catch (SQLException ex) {

            return false;
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
