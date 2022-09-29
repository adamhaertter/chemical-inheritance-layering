package datasource;
public class ChemicalDataGateway extends Gateway {
    
    private long id = 0;
    private String name = "";

    
    public ChemicalDataGateway(long id) {
        id = this.id;
        deleted = false;
    }

    public ChemicalDataGateway(String name) {
        // Since we are removing inhabits, we don't set that up here
        name = this.name;
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
