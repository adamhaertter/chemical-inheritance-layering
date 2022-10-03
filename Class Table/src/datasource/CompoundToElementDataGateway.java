package datasource;

public class CompoundToElementDataGateway extends Gateway {

    private long compoundId = 0;
    private long elementId = 0;

    /** only for finding what's already in the database **/
    public CompoundToElementDataGateway(long compoundId, long elementId) {
        super();
        this.elementId = elementId;
        this.compoundId = compoundId;
        deleted = false;
    }

    private boolean validate() {
        return this.compoundId != 0 && this.elementId != 0;
    }
}