package datasource;

public class ElementDataGateway extends ChemicalDataGateway {

    private int atomicNumber = 0, atomicMass = 0;

    /**
     * Reads a row corresponding to some Element id and creates a row data gateway from it
     * @param id the primary key id of Element
     */
    public ElementDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;
    }

    /**
     * Creates a new row in the database and a corresponding row data gateway for it
     * @param name the name field of the parent table Chemical
     * @param atomicNumber the atomic number field of Element
     * @param atomicMass the atomic mass field of Element
     */
    public ElementDataGateway(String name, int atomicNumber, int atomicMass) {
        super(name);
        // Since we are removing inhabits, we don't set that up here
        atomicNumber = this.atomicNumber;
        atomicMass = this.atomicMass;
        deleted = false;
    }

    private boolean validate() {
        return this.id != 0 && this.name != null && this.atomicNumber != 0 && this.atomicMass != 0;
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
