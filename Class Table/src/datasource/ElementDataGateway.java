package datasource;

public class ElementDataGateway extends ChemicalDataGateway {
    
    private long id = 0;
    private int atomicNumber = 0, atomicMass = 0;

    
    public ElementDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;
    }


    public ElementDataGateway(String name, int atomicNumber, int atomicMass) {
        super(name);
        // Since we are removing inhabits, we don't set that up here
        atomicNumber = this.atomicNumber;
        atomicMass = this.atomicMass;
        deleted = false;
    }

    /** getters and setters **/
    public int getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        verifyExistence();
        this.atomicNumber = atomicNumber;
    }

    public int getAtomicMass() {
        return atomicMass;
    }

    public void setAtomicMass(int atomicMass) {
        verifyExistence();
        this.atomicMass = atomicMass;
    }
}
