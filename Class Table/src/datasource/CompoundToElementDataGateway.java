package datasource;

public class CompoundToElementDataGateway extends Gateway {

    private long compoundId = 0;
    private long elementId = 0;

    public CompoundToElementDataGateway(long compoundId, long elementId) {
        elementId = this.elementId;
        compoundId = this.compoundId;
        deleted = false;
    }
}