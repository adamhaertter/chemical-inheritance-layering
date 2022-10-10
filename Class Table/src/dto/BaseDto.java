package dto;

public class BaseDto {

    public final long id, solute;
    public final String name;

    public BaseDto(long id, long solute, String name) {
        this.id = id;
        this.solute = solute;
        this.name = name;
    }
}
