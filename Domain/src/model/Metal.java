package model;

public class Metal extends Element {

    public Metal(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);
    }

    public Acid getDissolvedBy() {
        return null;
    }
}
