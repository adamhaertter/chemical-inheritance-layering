package datasource;

public class BaseDataGateway extends Gateway {

    private long id = 0;
    private int solute = 0;

   /** only used to create a data gateway for base **/
    BaseDataGateway(long id) {
        this.id = id;
        deleted = false;
    }

    /** used to create a new base **/
    BaseDataGateway(int solute) {
        this.solute = solute;
        deleted = false;
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