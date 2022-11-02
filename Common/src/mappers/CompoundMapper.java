package mappers;

import exceptions.CompoundNotFoundException;
import model.Compound;

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

    public Compound getMyCompound() {
        return myCompound;
    }
}
