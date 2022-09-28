import java.util.*;
import java.sql.*;

public class ChemicalDataGateways {
    long id = 0;
    String name = "";
    int atomicNumber = 0;
    double atomicMass = 0.0;
    int baseSolute = 0;
    int acidSolute = 0;
    String[] dissolved;
    long dissolvedBy = 0;
    boolean deleted = false;

    public static final String DB_LOCATION = "jdbc:mysql://45.77.144.116/phpmyadmin/index.php";
    public static final String LOGIN_NAME = "brennan";
    public static final String PASSWORD = "BgdGGZJQf1rPBcNb";
    protected static Connection m_dbConn = null;

    /**
     * Creates a connection to the database that you can then send commands to.
     */
    static {
        try {
            m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * To get the meta data for the DB.
     */
    DatabaseMetaData meta = m_dbConn.getMetaData();

    public ChemicalDataGateways(long identification) throws SQLException {
        id = identification;
    }

    public ChemicalDataGateways(String n, int number, double mass, int bSolute, int aSolute, String[] diss, long dissBy) {
        name = n;
        atomicNumber = number;
        atomicMass = mass;
        baseSolute = bSolute;
        acidSolute = aSolute;
        dissolved = diss;
        dissolvedBy = dissBy;
    }

    public void delete() {
        try {
            // code to delete from database
        } catch (Exception e) {
            // throw error about failing to delete
        }
        // does not hit unless sql delete is successful
        this.deleted = true;
    }

    private void verifyExistence() {
        if (deleted)
            try {
                throw new Exception("This gateway has been deleted.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    //Getters & Setters ----------------------------------------------------------------------
    public long getID() {
        verifyExistence();
        return id;
    }

    public String getName() {
        verifyExistence();
        return name;
    }

    public void setName(String name) {
        verifyExistence();
        this.name = name;
        persists();
    }

    public int getAtomicNumber() {
        verifyExistence();
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        verifyExistence();
        this.atomicNumber = atomicNumber;
        persists();
    }

    public double getAtomicMass() {
        verifyExistence();
        return atomicMass;
    }

    public void setAtomicMass(double atomicMass) {
        verifyExistence();
        this.atomicMass = atomicMass;
        persists();
    }

    public int getBaseSolute() {
        verifyExistence();
        return baseSolute;
    }

    public void setBaseSolute(int baseSolute) {
        verifyExistence();
        this.baseSolute = baseSolute;
        persists();
    }

    public int getAcidSolute() {
        verifyExistence();
        return acidSolute;
    }

    public void setAcidSolute(int acidSolute) {
        verifyExistence();
        this.acidSolute = acidSolute;
        persists();
    }

    public String[] getDissolved() {
        verifyExistence();
        return dissolved;
    }

    public void setDissolved(String[] dissolved) {
        verifyExistence();
        this.dissolved = dissolved;
        persists();
    }

    public long getDissolvedBy() {
        verifyExistence();
        return dissolvedBy;
    }

    public void setDissolvedBy(long dissolvedBy) {
        verifyExistence();
        this.dissolvedBy = dissolvedBy;
        persists();
    }

    //Row Data Gateway ----------------------------------------------------------------------

    public void chemicalRowDataGateway(long id) {
        this.id = id;


    }

    public void chemicalRowDataGateway(String n, int number, double mass, int bSolute, int aSolute,
                                       String[] diss, long dissBy) {
        this.name = n;
        this.atomicNumber = number;
        this.atomicMass = mass;
        this.baseSolute = bSolute;
        this.acidSolute = aSolute;
        this.dissolved = diss;
        this.dissolvedBy = dissBy;


    }

    public void getAllCompounds() {
        verifyExistence();

    }

    public void getMetalsDissolvedBy(int acidID) {
        verifyExistence();

    }

    public void getNonMetalsDissolvedBy(int acidID) {
        verifyExistence();

    }

    public void persists() {
        verifyExistence();

    }

    //Necessary in order for accessing the Database
    public static boolean activateJDBC() {
        try {
            Driver myDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(myDriver);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return true;
    }
}
