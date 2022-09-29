package datasource;

public class AcidDataGateway extends Gateway {
    
    private long id = 0;
    private int solute = 0;


    /**
     * Used to create a new row data gateway for an existing acid in the database
     * @param id
     */
    public AcidDataGateway(long id) {
        id = this.id;
        deleted = false;
    }

    /**
     * Used to create a new acid in the database and a row data gateway for it
     * @param solute
     */
    public AcidDataGateway(int solute) {
        solute = this.solute;
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
