package dto;

public class MetalDto {

    public final long id, dissolvedBy;
    public final String name;
    public final int atomicNumber, atomicMass;

    public MetalDto(long id, String name, int atomicNumber, int atomicMass, long dissolvedBy) {
        this.id = id;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.dissolvedBy = dissolvedBy;
    }
}
