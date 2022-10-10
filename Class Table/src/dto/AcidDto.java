package dto;

import datasource.MetalDataGateway;

import java.util.ArrayList;

public class AcidDto {

    public final long id, solute;
    public final String name;
    public final ArrayList<Long> dissolves;

    public AcidDto(long id, String name, long solute) {
        this.id = id;
        this.solute = solute;
        this.name = name;
        dissolves = MetalDataGateway.getAllDissolvedBy(id);
    }
}
