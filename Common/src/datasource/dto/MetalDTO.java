package datasource.dto;

public class MetalDTO {
    public long id;
    public String name;
    public int atomicNumber;
    public double atomicMass;
    public long dissolvedBy;

    public MetalDTO(long id, String name, int atomicNumber, double atomicMass, long dissolvedBy) {
        this.id = id;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.dissolvedBy = dissolvedBy;
    }
}
