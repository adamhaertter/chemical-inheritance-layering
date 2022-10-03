package datasource;
public class ChemicalDataGateway extends Gateway {
    
    private long id = 0;
    protected String name = "";

    
    public ChemicalDataGateway(long id) {
        super();
        this.id = id;
        deleted = false;
    }

    public ChemicalDataGateway(String name) {
        super();
        // Since we are removing inhabits, we don't set that up here
        this.name = name;
        deleted = false;
    }

    /** getters and setters **/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        verifyExistence();
        this.name = name;
    }
}
