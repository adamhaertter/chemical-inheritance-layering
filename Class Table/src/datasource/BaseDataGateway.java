package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BaseDataGateway extends ChemicalDataGateway {

    private long solute = 0;

   /**
    * Only used to create a data gateway for base that already exists
    * @param id the primary key id
    */
    public BaseDataGateway(long id) {
        super(id);
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
     * @param name the name field of the parent table, Chemical
     * @param solute the solute field of the Base table
     */
    public BaseDataGateway(String name, long solute) {
        super(name);
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
    public long getSolute() {
        if(verify())
            return solute;
        else
            return -1;
    }

    public void setSolute(long solute) {
        if( !verify() )
            return;
        this.solute = solute;
        persist(id, name);
    }
}