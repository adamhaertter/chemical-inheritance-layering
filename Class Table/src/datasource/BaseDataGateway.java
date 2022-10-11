package datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Contains both the Row Data Gateway and Table Data Gateway functionality for the Base table. Row functions are done
 * by an instance of this class, while the table functions are static methods.
 *
 * Extends the ChemicalDataGateway class so the implementation works across the multiple inherited tables for
 * Class Table Inheritance.
 */
public class BaseDataGateway extends ChemicalDataGateway {

    private long solute = 0;

   /**
    * Only used to create a data gateway for base that already exists
    * @param conn connection to the DB
    * @param id the primary key id
    */
    public BaseDataGateway(Connection conn, long id) {
        super(conn, id);
        this.id = id;
        deleted = false;

        //Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Base WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.solute = rs.getLong("solute");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                System.out.println("No base was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Used to create a new base and add it to the database
     * @param conn connection to the DB
     * @param name the name field of the parent table, Chemical
     * @param solute the solute field of the Base table
     */
    public BaseDataGateway(Connection conn, String name, long solute) {
        super(conn, name);
        this.solute = solute;
        deleted = false;

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Base" +
                    "(id, solute) VALUES ('" +
                    id + "','" + solute + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks the validity of the information in the row and the corresponding parent rows.
     *
     * @return Whether the current columns for this row have valid values
     */
    protected boolean validate() {
        return super.validate() && this.solute != 0;
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
            statement.executeUpdate("UPDATE Base SET solute = '" + solute +
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
     * @see ChemicalDataGateway#setName(String) for line-by-line comments
     */
    public void setSolute(long solute) {
        if( !verify() && !persist(this.id, this.name, solute) )
            return;
        this.solute = solute;
    }
}