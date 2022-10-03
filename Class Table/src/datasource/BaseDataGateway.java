package datasource;

public class BaseDataGateway extends Gateway {

    private long id = 0;
    private int solute = 0;

   /** only used to create a data gateway for base **/
    BaseDataGateway(long id) {
        super();
        this.id = id;
        deleted = false;
        //Read from DB
    }

    /** used to create a new base **/
    BaseDataGateway(int solute) {
        super();
        this.solute = solute;
        deleted = false;
        // Create in DB
    }

    public void persists() {
        // Update in DB
    }

    public void delete() {
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