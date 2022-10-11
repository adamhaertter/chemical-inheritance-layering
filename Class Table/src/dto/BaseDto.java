package dto;

/**
 * Immutable Data Transfer Object for the information in the Base table.
 */
public class BaseDto {

    public final long id, solute;
    public final String name;

    /**
     * Creates the Data Transfer Object with the given information. This information cannot be changed later.
     *
     * @param id the unique id
     * @param name the name of the Chemical
     * @param solute the id of the solute
     */
    public BaseDto(long id, long solute, String name) {
        this.id = id;
        this.solute = solute;
        this.name = name;
    }
}
