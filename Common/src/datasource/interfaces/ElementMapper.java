package datasource.interfaces;

import model.Element;

public interface ElementMapper {
    /**
     * Create a new element in the database, and store the resulting model object
     * into my instance variable
     */
    void ElementMapper(String name, int atomicNumber, double atomicMass);

    /**
     * Constructor for objects that exist in the db
     * @param name
     */
    void ElementMapper(String name);

    Element getMyElement();
}
