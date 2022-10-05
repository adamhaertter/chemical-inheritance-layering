package dto;

public class MetalDTO {
    public long id;
    public String name;
    public long atomicNumber;
    public long atomicMass;
    public long dissolvedBy;

    public MetalDTO(long id, String name, long atomicNumber, long atomicMass, long dissolvedBy) {
        this.id = id;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.dissolvedBy = dissolvedBy;
    }
}
