package dto;

public class ElementDTO {
    public long id;
    public String name;
    public double atomicNumber;
    public double atomicMass;

    public ElementDTO(long id, String name, double atomicNumber, double atomicMass) {
        this.id = id;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
    }
}