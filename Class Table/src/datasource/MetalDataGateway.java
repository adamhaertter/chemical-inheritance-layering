package datasource;

public class MetalDataGateway extends ElementDataGateway {

    private long dissolvedBy = 0;

    /**
     * Constructs a row data gateway based on an existing id in the database
     * @param id primary key id
     */
    public MetalDataGateway(long id) {
        super(id);
    }

    /**
     * Creates a row data gateway and a new instance of Metal in the DB and fills the given information into the appropriate tables.
     * @param name the name field of parent table Chemical
     * @param atomicNumber the atomicNumber field of parent table Element
     * @param atomicMass the atomicMass field of parent table Element
     * @param dissolvedByAcid the dissolvedBy foreign key of the Metal Table
     */
    public MetalDataGateway(String name, int atomicNumber, int atomicMass, long dissolvedByAcid) {
        super(name, atomicNumber, atomicMass);
        dissolvedBy = dissolvedByAcid;
    }

    // I'm not sure if this constructor will be useful in the long term
    public MetalDataGateway(long elementId, long dissolvedByAcid) {
        super(elementId);
        dissolvedBy = dissolvedByAcid;
    }

    /** getters and setters **/
    public long getDissolvedBy() {
        return dissolvedBy;
    }

    public void setDissolvedBy(long dissolvedBy) {
        verifyExistence();
        this.dissolvedBy = dissolvedBy;
    }
}
