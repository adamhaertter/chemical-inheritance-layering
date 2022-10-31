package mappers;

import exceptions.ElementNotFoundException;
import model.Element;

public abstract class ElementMapper
{
    private Element myElement;
    /**
     * Create a new element in the database, and store the resulting model object
     * into my instance variable
     */
    public ElementMapper(String name, int atomicNumber, double atomicMass)
    {
        myElement = new Element(name, atomicNumber, atomicMass);
    }

    /**
     * Constructor for objects that exist in the db
     * @param name
     */
    public ElementMapper(String name) throws ElementNotFoundException
    {
        myElement = new Element(name);
    }

    public Element getMyElement()
    {
        return myElement;
    }
}
