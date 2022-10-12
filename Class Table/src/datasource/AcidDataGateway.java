package datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Contains both the Row Data Gateway and Table Data Gateway functionality for the Acid table. Row functions are done
 * by an instance of this class, while the table functions are static methods.
 *
 * Extends the ChemicalDataGateway class so the implementation works across the multiple inherited tables for
 * Class Table Inheritance.
 */
public class AcidDataGateway extends ChemicalDataGateway {

    private long solute = 0;
    private ArrayList<Long> dissolves;

    /**
     * Used to create a new row data gateway for an existing acid in the database
     * @param conn connection to the DB
     * @param id The id primary key in the db
     */
    public AcidDataGateway(Connection conn, long id) {
        super(conn, id);
        this.id = id;
        deleted = false;

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Acid WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.solute = rs.getLong("solute");

            if (!validateAcid()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                System.out.println("No acid was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dissolves = MetalDataGateway.getAllDissolvedBy(id);
    }

    /**
     * Used to create a new acid in the database and a row data gateway for it
     * @param conn connection to the DB
     * @param name the name field of the parent table, Chemical
     * @param solute the solute field of the db
     */
    public AcidDataGateway(Connection conn, String name, long solute) {
        super(conn, name);
        this.solute = solute;
        deleted = false;

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Acid" +
                    "(id, solute) VALUES ('" +
                    id + "','" + solute + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dissolves = MetalDataGateway.getAllDissolvedBy(id);
    }

    /**
     * Checks the validity of the information in the row and the corresponding parent rows.
     *
     * @return Whether the current columns for this row have valid values
     */
    protected boolean validateAcid() {
        return validateChemical() && this.solute != 0;
    }

    /**
     * Updates the database with the values stored to the instance variables of the gateway.
     * Cascades upward to all parent tables.
     * @return Whether the update is passed correctly.
     */
    protected boolean persist(long id, String name, long solute) {
        super.persist(id, name);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Acid SET solute = '" + solute +
                    "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** getters and setters **/

    /**
     * Checks that the row is valid, then returns solute FK
     * @return id of the solute, -1 if invalid
     */
    public long getSolute() {
        if(verify())
            return solute;
        else
            return -1;
    }

    /**
     * Updates the solute id in the database. A message will be printed if this does not occur.
     *
     * @param solute the solute id with which to update the DB
     * @see ChemicalDataGateway#updateName(String) for line-by-line comments
     */
    public void updateSolute(long solute) {
        // If the row exists AND we can update the values in the DB...
        if( verify() && persist(this.id, this.name, solute) )
            this.solute = solute;
        // Set the instance variable to match
    }

    /**
     * @return metal ids dissolved by this Acid
     */
    public ArrayList<Long> getDissolves() {
        return dissolves;
    }
}
