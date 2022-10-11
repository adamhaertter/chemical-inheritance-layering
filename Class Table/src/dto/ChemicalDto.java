package dto;

/**
 * Immutable Data Transfer Object for the information in the Chemical table.
 *
 * @note Instances of Compound should be sent as ChemicalDto as the information is identical. If instances of Compound
 * will need to carry more information in the future, a separate DTO can be implemented at that time.
 */
public class ChemicalDto {

    public final long id;
    public final String name;

    /**
     * Creates the Data Transfer Object with the given information. This information cannot be changed later.
     *
     * @param id the unique id
     * @param name the name of the Chemical
     */
    public ChemicalDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
