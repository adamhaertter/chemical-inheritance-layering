package dto;

public class CompoundToElementDTO {
    public long compoundId;
    public long elementId;

    public CompoundToElementDTO(long compoundId, long elementId) {
        this.compoundId = compoundId;
        this.elementId = elementId;
    }
}