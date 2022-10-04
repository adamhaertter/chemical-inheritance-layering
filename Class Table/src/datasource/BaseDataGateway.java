package datasource;

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

    private boolean validate() {
        return this.id != 0 && this.name != null && this.solute != 0;
    }

    public void persist() {
        // Update in DB
    }

    /** getters and setters **/
    public int getSolute() {
        return solute;
    }

    public void setSolute(long solute) {
        verifyExistence();
        this.solute = solute;
    }
}