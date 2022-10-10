package dto;

public class ElementDto {

    public final long id;
    public final String name;
    public final int atomicNumber;
    public final double atomicMass;

    public ElementDto(long id, String name, double atomicMass, int atomicNumber) {
        this.id = id;
        this.name = name;
        this.atomicMass = atomicMass;
        this.atomicNumber = atomicNumber;
    }
}
