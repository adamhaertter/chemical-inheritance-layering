package datasource;

public class MetalDataGateway extends ElementDataGateway {

    private long dissolvedBy = 0;

    public MetalDataGateway(long id) {
        super(id);
    }

    public MetalDataGateway(int atomicNumber, int atomicMass, long dissolvedByAcid) {
        super(atomicNumber, atomicMass);
        dissolvedBy = dissolvedByAcid;
    }

    // I'm not sure if this constructor will be useful in the long term
    public MetalDataGateway(long elementId, long dissolvedByAcid) {
        super(elementId);
        dissolvedBy = dissolvedByAcid;
    }
    
}
