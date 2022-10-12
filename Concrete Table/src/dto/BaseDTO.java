package dto;

public class BaseDTO {
    public long id;
    public String name;
    public double solute;

    public BaseDTO(long id, String name, double solute) {
        this.id = id;
        this.name = name;
        this.solute = solute;
    }
}
