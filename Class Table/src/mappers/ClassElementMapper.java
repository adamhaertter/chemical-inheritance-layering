package mappers;

import datasource.*;
import exceptions.ElementNotFoundException;
import model.Element;

import java.sql.Connection;
import java.util.ArrayList;

public class ClassElementMapper extends ElementMapper {

    private ElementDataGateway gateway;

    /**
     * Create a new element in the database, and store the resulting model object
     * into my instance variable
     */
    public ClassElementMapper(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);

        Connection conn = Gateway.setUpConnection();
        this.gateway = new ElementDataGateway(conn, name, atomicNumber, atomicMass);

        myElement = new Element(this, name, atomicNumber, atomicMass);

//        try {
//            assert conn != null;
//            conn.close();
//        } catch (SQLException ignored) {
//        }
    }

    /**
     * Constructor for objects that exist in the db
     * @param name The name of the element to search for
     */
    public ClassElementMapper(String name) throws ElementNotFoundException {
        super(name);

        long myId = ChemicalDataGateway.getIdByName(name);
        Connection conn = Gateway.setUpConnection();
        if(myId == -1) {
            throw new ElementNotFoundException("No element found with name " + name);
        }
        this.gateway = new ElementDataGateway(conn, myId);

        myElement = new Element(this, name, gateway.getAtomicNumber(), gateway.getAtomicMass());

//        try {
//            conn.close();
//        } catch (SQLException e) {
//        }
    }

    /**
     * Updates the database with the given information by calling upon the Gateway
     * @param name name of the element
     * @param atomicNumber atomic number of the element
     * @param atomicMass atomic mass of the element
     */
    @Override
    public void persists(String name, int atomicNumber, double atomicMass) {
        this.gateway.persist(gateway.getId(), name, atomicMass, atomicNumber);
    }

    /**
     * Deletes the row from the database
     */
    @Override
    public void delete() {
        this.gateway.delete();
    }

    @Override
    public ArrayList<String> getCompoundsContaining() {
        ArrayList compoundNames = new ArrayList();
        long id = gateway.getId();
        ArrayList<Long> compoundIds = CompoundToElementDataGateway.getCompoundsContaining(id);
        Connection conn2 = Gateway.setUpConnection();
        for(long compound : compoundIds) {
            compoundNames.add((new CompoundDataGateway(conn2, compound)).getName());
        }
        return compoundNames;
    }

    public static ArrayList<Element> getAllElements(){
        ArrayList<Element> elements = new ArrayList<>();
        for(String element : ElementDataGateway.getElements()) {
            try {
                elements.add((new ClassElementMapper(element)).getMyElement());
            } catch (ElementNotFoundException e) {
                // The database must have been updated while making the call.
            }
        }
        return elements;
    }

    public static ArrayList<Element> getElementsBetween(int firstNum, int lastNum){
        ArrayList<Element> elements = new ArrayList<Element>();
        for(String element : ElementDataGateway.getElements()) {
            try {
                elements.add((new ClassElementMapper(element)).getMyElement());
            } catch (ElementNotFoundException e) {
                // The database must have been updated while making the call.
            }
        }
        return elements;
    }
}
