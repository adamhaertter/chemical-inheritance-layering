package model;

public class Element extends Chemical
{
    private String name;
    private int atomicNumber;
    private double atomicMass;
    public Element(String name, int atomicNumber, double atomicMass) {
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
    }

    public Element(String name) {
        this.name = name;
    }

    public int getAtomicNumber()
    {
        return 0;
    }

    public double getAtomicMass()
    {
        return 0;
    }
}
