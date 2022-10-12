package dto;

/**
 * Immutable Data Transfer Object for the information in the relation table for Compound and Element. Carries one id
 * pair from the many-to-many relationship.
 */
public class CompoundToElementDto {

    public final long elementId, compoundId;

    /**
     * Creates the Data Transfer Object with the given information. This information cannot be changed later.
     *
     * @param elementId the unique id of the element in the relation
     * @param compoundId the unique id of the compound in the relation
     */
    public CompoundToElementDto(long elementId, long compoundId) {
        this.elementId = elementId;
        this.compoundId = compoundId;
    }
}
