package model;

import mappers.ElementMapper;

public class Element extends Chemical
{
    public final ElementMapper mapper;
    private int atomicNumber;
    private double atomicMass;

    public Element(ElementMapper mapper, String name, int atomicNumber, double atomicMass) {
        super(name);
        this.mapper = mapper;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
    }

    public Element(ElementMapper mapper, String name) {
        super(name);
        this.mapper = mapper;
    }

    public int getAtomicNumber()
    {
        return atomicNumber;
    }

    public double getAtomicMass()
    {
        return atomicMass;
    }

    public void setAtomicNumber(int atomicNumber)
    {
        this.atomicNumber = atomicNumber;
    }

    public void setAtomicMass(double atomicMass)
    {
        this.atomicMass = atomicMass;
    }

    public int getPeriod()
    {
        int period = 7; // Default case
        int[] periodStartPoint = {1, 3, 11, 19, 37, 55, 87};
        for(int i = 0; i < periodStartPoint.length-1; i++) {
            if(atomicNumber >= periodStartPoint[i] && atomicNumber < periodStartPoint[i+1]) {
                period = i + 1;
                break;
            }
        }
        return period;
    }

    public void persist()
    {
        mapper.persists(name, atomicNumber, atomicMass);
    }

    public void delete() {
        mapper.delete();
    }
}
