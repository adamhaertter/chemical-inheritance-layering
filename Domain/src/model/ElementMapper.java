package model;

public class ElementMapper
{
    private Element myElement;
    /**
     * Create a new element in the database, and store the resulting model object
     * into my instance variable
     */
    public ElementMapper(String name, int atomicNumber, double atomicMass)
    {
    }

    /**
     * Constructor for objects that exist in the db
     * @param name
     */
    public ElementMapper(String name)
    {
    }

    public Element getMyElement()
    {
        return myElement;
    }
}
