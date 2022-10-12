package dto;

/**
 *
 */
public class CompoundToElementDTO {
    public long compoundID;
    public long elementID;

    /**
     *
     * @param compoundID - the identification of the compound
     * @param elementID - the identification of the element
     */
    public CompoundToElementDTO(long compoundID, long elementID) {
        this.compoundID = compoundID;
        this.elementID = elementID;
    }
}
