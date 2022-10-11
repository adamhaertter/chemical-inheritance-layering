package dto;

/**
 * Immutable Data Transfer Object for the information in the Metal table.
 */
public class MetalDto {

    public final long id, dissolvedBy;
    public final String name;
    public final int atomicNumber;
    public final double atomicMass;

    /**
     * Creates the Data Transfer Object with the given information. This information cannot be changed later.
     *
     * @param id the unique id
     * @param name the name of the Chemical
     * @param atomicMass the atomic mass of the element
     * @param atomicNumber the atomic number of the element
     * @param dissolvedBy the id of the acid that dissolves this metal
     */
    public MetalDto(long id, String name, int atomicNumber, double atomicMass, long dissolvedBy) {
        this.id = id;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.dissolvedBy = dissolvedBy;
    }
}
