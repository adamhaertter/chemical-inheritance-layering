package datasource;

public class BaseDataGateway extends ChemicalDataGateway {

    private long id = 0;
    private int solute = 0;

   /** only used to create a data gateway for base **/
    public BaseDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;
        //Read from DB
    }

    /** used to create a new base **/
    public BaseDataGateway(String name, int solute) {
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

    public void setSolute(int solute) {
        verifyExistence();
        this.solute = solute;
    }
}