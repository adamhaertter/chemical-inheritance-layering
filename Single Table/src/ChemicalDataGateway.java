import java.sql.*;

public class ChemicalDataGateway extends Gateway {
    long id = 0;
    String name = "";
    int atomicNumber = 0;
    double atomicMass = 0.0;
    int baseSolute = 0;
    int acidSolute = 0;
    String[] dissolves;
    long dissolvedBy = 0;
    String type = "";

    /**
     * Constructor that uses the id to create a row data gateway
     *
     * @param identification - the id for each chemical
     * @throws SQLException
     */
    public ChemicalDataGateway(long identification) throws SQLException {
        super();
        this.id = identification;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM 'Chemical' WHERE id = '" + id + "'");
            ResultSet rs = statement.executeQuery();
            this.name = rs.getString("name");
            this.atomicNumber = rs.getInt("atomicNumber");
            this.atomicMass = rs.getInt("atomicMass");
            this.baseSolute = rs.getInt("baseSolute");
            this.acidSolute = rs.getInt("acidSolute");
            this.dissolvedBy = rs.getLong("dissolvedBy");
            this.type = rs.getString("type");

            statement = conn.prepareCall("SELECT dissolvedBy FROM 'Chemical'" +
                                                "WHERE id = '" + id + "'");
            rs = statement.executeQuery();
            for(int i = 0; rs.next(); i++) {
                dissolves[i] = rs.getString(i);
            }
            persist();
        } catch(Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor used to assign values to a list of variables
     * @param n - the name of the chemical
     * @param number - the atomic number of the chemical
     * @param mass - the atomic mass of the chemical
     * @param bSolute - the base solute of the chemical
     * @param aSolute - the acid solute of the chemical
     * @param diss - the list of chemicals that are capable of being dissolved
     * @param dissBy - the acid that a chemical is dissolved by
     * @param type -
     */
    public ChemicalDataGateway(String n, int number, double mass, int bSolute,
                               int aSolute, String[] diss, long dissBy, String type) {
        super();
        this.name = n;
        this.atomicNumber = number;
        this.atomicMass = mass;
        this.baseSolute = bSolute;
        this.acidSolute = aSolute;
        this.dissolves = diss;
        this.dissolvedBy = dissBy;
        this.type = type;

        

        persist();
    }

    //Getters & Setters ----------------------------------------------------------------------
    /**
     * Getter for the id
     * @return id - the identification
     */
    public long getID() {
        verifyExistence();
        return id;
    }

    /**
     * Getter for the name
     * @return name - the name of the chemical
     */
    public String getName() {
        verifyExistence();
        return name;
    }

    /**
     * Setter for the name
     * @param name - the name of the chemical
     */
    public void setName(String name) {
        verifyExistence();
        this.name = name;
        persist();
    }

    /**
     * Getter for the atomic number
     * @return atomicNumber - the atomic number of the chemical
     */
    public int getAtomicNumber() {
        verifyExistence();
        return atomicNumber;
    }

    /**
     * Setter for the atomic number
     * @param atomicNumber - the atomic number of the chemical
     */
    public void setAtomicNumber(int atomicNumber) {
        verifyExistence();
        this.atomicNumber = atomicNumber;
        persist();
    }

    /**
     * Getter for the atomic mass
     * @return atomicMass - the atomic mass of the chemical
     */
    public double getAtomicMass() {
        verifyExistence();
        return atomicMass;
    }

    /**
     * Setter for the atomic mass
     * @param atomicMass - the atomic mass of the chemical
     */
    public void setAtomicMass(double atomicMass) {
        verifyExistence();
        this.atomicMass = atomicMass;
        persist();
    }

    /**
     * Getter for the base solute
     * @return baseSolute - the base solute of the chemical
     */
    public int getBaseSolute() {
        verifyExistence();
        return baseSolute;
    }

    /**
     * Setter for the base solute
     * @param baseSolute - the base solute of the chemical
     */
    public void setBaseSolute(int baseSolute) {
        verifyExistence();
        this.baseSolute = baseSolute;
        persist();
    }

    /**
     * Getter for the acid solute
     * @return acidSolute - the acid solute of the chemical
     */
    public int getAcidSolute() {
        verifyExistence();
        return acidSolute;
    }

    /**
     * Set acid solute
     * @param acidSolute - the acid solute of the chemical
     */
    public void setAcidSolute(int acidSolute) {
        verifyExistence();
        this.acidSolute = acidSolute;
        persist();
    }

    /**
     * Getter for dissolves
     * @return dissolves - the list of chemicals that are capable of being dissolved
     */
    public String[] getDissolves() {
        verifyExistence();
        return dissolves;
    }

    /**
     * Setter for dissolves
     * @param dissolves - the list of chemicals that are capable of being dissolved
     */
    public void setDissolves(String[] dissolves) {
        verifyExistence();
        this.dissolves = dissolves;
        persist();
    }

    /**
     * Getter for dissolvedBy
     * @return dissolvedBy - the acid that a chemical is dissolved by
     */
    public long getDissolvedBy() {
        verifyExistence();
        return dissolvedBy;
    }

    /**
     * Setter for dissolvedBy
     * @param dissolvedBy - the acid that a chemical is dissolved by
     */
    public void setDissolvedBy(long dissolvedBy) {
        verifyExistence();
        this.dissolvedBy = dissolvedBy;
        persist();
    }

    /**
     * Getter for dissolvedBy
     * @return type - the acid that a chemical is dissolved by
     */
    public String getType() {
        verifyExistence();
        return type;
    }

    /**
     * Setter for type
     * @param type - the acid that a chemical is dissolved by
     */
    public void setType(String type) {
        verifyExistence();
        this.type = type;
        persist();
    }

    /**
     *
     * @return true or false
     */
    public boolean persist() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Chemical SET id = '" + id + "', name = '" + name + "', " +
                    "atomicNumber = '" + atomicNumber + "', atomicMass = '" + atomicMass + "', baseSolute = '"
                    + baseSolute + "', acidSolute = '" + acidSolute + "', dissolves = '" + dissolves +
                    "', dissolvedBy = '" + dissolvedBy + "', type = '" + type + "' WHERE id = '" + id + "'");
        } catch (Exception ex) {
            // Fails because already exists
            return false;
        }
        return true;
    }

    /**
     *
     */
    private void verifyExistence() {
        if (deleted)
            try {
                throw new Exception("This item has been deleted.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
