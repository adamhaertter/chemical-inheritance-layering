package datasource;

public class AcidDataGateway extends ChemicalDataGateway {

    private int solute = 0;

    /**
     * Used to create a new row data gateway for an existing acid in the database
     * @param id The id primary key in the db
     */
    public AcidDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;
    }

    /**
     * Used to create a new acid in the database and a row data gateway for it
     * @param name the name field of the parent table, Chemical
     * @param solute the solute field of the db
     */
    public AcidDataGateway(String name, int solute) {
        super(name);
        this.solute = solute;
        deleted = false;
    }

    private boolean validate() {
        return this.id != 0 && this.name != null && this.solute != 0;
    }

    /** getters and setters **/
    public int getSolute() {
        return solute;
    }

    public void setSolute(int solute) {
        verifyExistence();
        this.solute = solute;
    }
}
