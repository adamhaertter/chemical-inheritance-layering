package mappers;

import exceptions.ElementNotFoundException;
import model.Element;

import java.sql.Connection;

public abstract class ElementMapper
{
    protected Element myElement;
    protected Connection conn;
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
    public ElementMapper(String name) throws ElementNotFoundException
    {

    }

    public Element getMyElement()
    {
        return myElement;
    }

    public abstract void persists(String name, int atomicNumber, double atomicMass);
}
