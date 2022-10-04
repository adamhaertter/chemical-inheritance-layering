package datasource;

public class AcidDataGateway extends ChemicalDataGateway {

    private long solute = 0;

    /**
     * Used to create a new row data gateway for an existing acid in the database
     * @param id The id primary key in the db
     */
    public AcidDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;
        // Read from DB
    }

    /**
     * Used to create a new acid in the database and a row data gateway for it
     * @param name the name field of the parent table, Chemical
     * @param solute the solute field of the db
     */
    public AcidDataGateway(String name, long solute) {
        super(name);
        this.solute = solute;
        deleted = false;
        // Create in DB
    }

    private boolean validate() {
        return this.id != 0 && this.name != null && this.solute != 0;
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
