package mappers;

import exceptions.CompoundNotFoundException;
import exceptions.ElementNotFoundException;
import model.Compound;

import java.util.ArrayList;

public abstract class CompoundMapper {

    protected Compound myCompound;
    /**
     * Constructor for objects that exist in the db
     * @param name
     */
    public CompoundMapper(String name) throws CompoundNotFoundException {

    }

    /**
     * Create a new compound in the database, and store the resulting model object
     * into my instance variable
     */
    public static void createCompound(String name) {

    }

    public abstract void addElement(String name) throws ElementNotFoundException;

    public Compound getMyCompound() {
        return myCompound;
    }

    public abstract void persists(String name);

    public abstract void delete();

    public abstract ArrayList<String> getElementsInCompound();
}
