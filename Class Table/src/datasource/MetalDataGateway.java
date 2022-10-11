package datasource;

import java.sql.*;
import java.util.ArrayList;

/**
 * Contains both the Row Data Gateway and Table Data Gateway functionality for the Metal table. Row functions are done
 * by an instance of this class, while the table functions are static methods.
 *
 * Extends the ElementDataGateway class, and by extension the ChemicalDataGateway class, so the implementation works
 * across the multiple inherited tables for Class Table Inheritance.
 */
public class MetalDataGateway extends ElementDataGateway {

    private long dissolvedBy = 0;

    /**
     * Constructs a row data gateway based on an existing id in the database
     * @param conn connection to the DB
     * @param id primary key id
     */
    public MetalDataGateway(Connection conn, long id) {
        super(conn, id);

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Metal WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.dissolvedBy = rs.getInt("dissolvedBy");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.dissolvedBy = -1;
                System.out.println("No metal was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a row data gateway and a new instance of Metal in the DB and fills the given information into the appropriate tables.
     * @param conn connection to the DB
     * @param name the name field of parent table Chemical
     * @param atomicNumber the atomicNumber field of parent table Element
     * @param atomicMass the atomicMass field of parent table Element
     * @param dissolvedByAcid the dissolvedBy foreign key of the Metal Table
     */
    public MetalDataGateway(Connection conn, String name, int atomicNumber, double atomicMass, long dissolvedByAcid) {
        super(conn, name, atomicNumber, atomicMass);
        dissolvedBy = dissolvedByAcid;

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Metal" +
                    "(id, dissolvedBy) VALUES ('" +
                    id + "','" + dissolvedByAcid + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * Verifies that the dissolvedBy variable is a valid number.
     * @return true if the row is full of valid values, false otherwise
     */
    protected boolean validate() {
        return super.validate() && this.dissolvedBy > 0;
    }

    /**
     * Updates the database with the values stored to the instance variables of the gateway.
     * Cascades upward to all parent tables.
     * @return Whether the update is passed correctly.
     */
    protected boolean persist(long id, String name, double atomicMass, int atomicNumber, long dissolvedBy) {
        super.persist(id, name, atomicMass, atomicNumber);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Metal SET dissolvedBy = '" + dissolvedBy +
                    "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Queries the database for all metals dissolved by a given acid id and returns said ids as an ArrayList of longs.
     *
     * @param acidId the id of the acid that dissolves these metals
     * @return an ArrayList of metal ids
     */
    public static ArrayList<Long> getAllDissolvedBy(long acidId) {
        ArrayList<Long> metals = new ArrayList<Long>();
        try {
            Connection conn = setUpConnection();

            CallableStatement st = conn.prepareCall("SELECT * FROM Metal WHERE dissolvedBy = ?");
            st.setLong(1, acidId);
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            while(rs.next()){
                metals.add(rs.getLong("dissolvedBy"));
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metals;
    }

    /** getters and setters **/

    /**
     * Checks that the row is valid, then returns dissolvedBy FK
     * @return id of the acid that dissolves this metal, -1 if invalid
     */
    public long getDissolvedBy() {
        if(verify())
            return dissolvedBy;
        else
            return -1;
    }

    /**
     * Updates the reference to the id of the acid that dissolves this metal in the database. A message will be
     * printed if this does not occur.
     *
     * @param dissolvedBy the acid's id that dissolves this metal with which to update the DB
     * @see ChemicalDataGateway#UpdateName(String) for line-by-line comments
     */
    public void updateDissolvedBy(long dissolvedBy) {
        if( !verify() && !persist(this.id, this.name, this.atomicMass, this.atomicNumber, dissolvedBy))
            return;
        this.dissolvedBy = dissolvedBy;
    }
}
