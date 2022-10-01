package src;

public class Compound {
    long compoundID;
    long elementID;

    public Compound(long compound) {
        this.compoundID = compound;
    }

    public Compound(long compound, long element) {
        this.compoundID = compound;
        this.elementID = element;
    }

    public long getCompoundID() {
        return compoundID;
    }

    public void setCompoundID(long compoundID) {
        this.compoundID = compoundID;
    }

    public long getElementID() {
        return elementID;
    }

    public void setElementID(long elementID) {
        this.elementID = elementID;
    }
}
