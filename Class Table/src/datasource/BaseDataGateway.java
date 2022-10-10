package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;

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
    }

    protected boolean validate() {
        return super.validate() && this.solute != 0;
    }

    public void persist() {
        // Update in DB
    }

    /** getters and setters **/
    public long getSolute() {
        return solute;
    }

    public void setSolute(long solute) {
        verifyExistence();
        this.solute = solute;
    }
}