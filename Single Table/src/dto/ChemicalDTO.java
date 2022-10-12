package dto;

/**
 *
 */
public class ChemicalDTO {
    public long id = 0;
    public String name = "";
    public int atomicNumber = 0;
    public double atomicMass = 0.0;
    public long baseSolute = 0;
    public long acidSolute = 0;
    public long dissolvedBy = 0;
    public String type = "";

    /**
     *
     * @param id - the identification
     * @param name - the name of the chemical
     * @param atomicNumber - the atomic number of the chemical
     * @param atomicMass - the atomic mass of the chemical
     * @param baseSolute - the base solute of the chemical
     * @param acidSolute - the acid solute of the chemical
     * @param dissolvedBy - the acid that a chemical is dissolved by
     * @param type - Type of Chemical (i.e. Metal, Nonmetal, etc.)
     */
    public ChemicalDTO(long id, String name, int atomicNumber, double atomicMass, long baseSolute, long acidSolute,
                       long dissolvedBy, String type) {
        this.id = id;
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.baseSolute = baseSolute;
        this.acidSolute = acidSolute;
        this.dissolvedBy = dissolvedBy;
        this.type = type;
    }
}