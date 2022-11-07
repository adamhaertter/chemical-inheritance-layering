package mappers;

import exceptions.ElementNotFoundException;
import model.Element;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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

    public abstract void delete();

    public abstract ArrayList<String> getCompoundsContaining();

    public static List<Element> getAllElements(){
        // DUMMY method body
        // you have to write the child method for this!

        // We can't have a static abstract method, but we need to enforce the constructors
        // Therefore, we can't go to an interface and have to use an abstract class.
        return null;
    }

    public static ArrayList<Element> getElementsBetween(int firstNum, int lastNum) {
        /** @see ElementMapper.getAllElements() */
        return null;
    }
}
