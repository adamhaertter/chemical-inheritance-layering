package dto;

import datasource.MetalDataGateway;

import java.util.ArrayList;

/**
 * Immutable Data Transfer Object for the information in the Acid table.
 */
public class AcidDto {

    public final long id, solute;
    public final String name;
    public final ArrayList<Long> dissolves;

    /**
     * Creates the Data Transfer Object with the given information. This information cannot be changed later.
     *
     * @param id the unique id
     * @param name the name of the Chemical
     * @param solute the id of the solute
     */
    public AcidDto(long id, String name, long solute) {
        this.id = id;
        this.solute = solute;
        this.name = name;
        dissolves = MetalDataGateway.getAllDissolvedBy(id);
    }
}
