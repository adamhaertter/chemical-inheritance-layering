package datasource;

public class CompoundToElementDataGateway extends Gateway {

    private long compoundId = 0;
    private long elementId = 0;

    /** only for finding what's already in the database **/
    public CompoundToElementDataGateway(long compoundId, long elementId) {
        elementId = this.elementId;
        compoundId = this.compoundId;
        deleted = false;
    }
}