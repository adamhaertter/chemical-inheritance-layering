package dto;

public class CompoundToElementDto {

    public final long elementId, compoundId;

    public CompoundToElementDto(long elementId, long compoundId) {
        this.elementId = elementId;
        this.compoundId = compoundId;
    }
}
