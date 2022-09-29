package datasource;

public class ElementDataGateway extends Gateway {
    
    private long id = 0;
    private int atomicNumber = 0, atomicMass = 0;

    
    public ElementDataGateway(long id) {
        id = this.id;
        deleted = false;
    }

    public ElementDataGateway(int atomicNumber, int atomicMass) {
        // Since we are removing inhabits, we don't set that up here
        atomicNumber = this.atomicNumber;
        atomicMass = this.atomicMass;
        deleted = false;
    }
}
