package dto;

/**
 * Immutable Data Transfer Object for the information in the Element table.
 */
public class ElementDto {

    public final long id;
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
     */
    public ElementDto(long id, String name, double atomicMass, int atomicNumber) {
        this.id = id;
        this.name = name;
        this.atomicMass = atomicMass;
        this.atomicNumber = atomicNumber;
    }
}
