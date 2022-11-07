package mappers;

import datasource.ChemicalDataGateway;
import datasource.CompoundDataGateway;
import datasource.Gateway;
import exceptions.ElementNotFoundException;
import model.Element;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SingleElementMapper extends ElementMapper {
    private ChemicalDataGateway gateway;

    /**
     * Creates a new entry for the element in the database
     */
    public SingleElementMapper(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);

        Connection conn = Gateway.setUpConnection();
        this.gateway = new ChemicalDataGateway(conn, name, atomicNumber, atomicMass,
                0, 0, 0, "Element");
        myElement = new Element(this, name, atomicNumber, atomicMass);
    }

    /**
     * Constructor for elements that already exist
     * @param name of the element
     */
    public SingleElementMapper(String name) throws ElementNotFoundException {
        super(name);

        Connection conn = Gateway.setUpConnection();
        long myId = ChemicalDataGateway.getIdByName(conn, name);
        if(myId == -1) {
            throw new ElementNotFoundException("No element found with name " + name);
        }
        this.gateway = new ChemicalDataGateway(conn, myId);
        myElement = new Element(this, name, gateway.getAtomicNumber(), gateway.getAtomicMass());
    }

    /**
     * Updates the database with the given information by calling upon the Gateway
     * @param name name of the element
     * @param atomicNumber atomic number of the element
     * @param atomicMass atomic mass of the element
     */
    @Override
    public void persists(String name, int atomicNumber, double atomicMass) {
        this.gateway.persist(gateway.getId(), name, atomicNumber, atomicMass, gateway.getAcidSolute(),
                gateway.getBaseSolute(), gateway.getDissolvedBy(), gateway.getType());
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
        ArrayList<Long> compoundIds = CompoundDataGateway.getCompoundsContaining(id);
        Connection conn2 = Gateway.setUpConnection();

        for(long compound : compoundIds) {
            compoundNames.add((new CompoundDataGateway(conn2, compound)).getName());
        }
        try {
            conn2.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return compoundNames;
    }

    public static ArrayList<Element> getAllElements(){
        ArrayList<Element> elements = new ArrayList<>();
        Connection shared = Gateway.setUpConnection();
        for(String element : ChemicalDataGateway.getElements(shared)) {
            try {
                elements.add((new SingleElementMapper(element)).getMyElement());
            } catch (ElementNotFoundException e) {
                // The database must have been updated while making the call.
            }
        }
        try {
            shared.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    public static ArrayList<Element> getElementsBetween(int firstNum, int lastNum){
        Connection shared = Gateway.setUpConnection();
        ArrayList<Element> elements = new ArrayList<Element>();
        for(String element : ChemicalDataGateway.getElementsBetween(shared, firstNum, lastNum)) {
            try {
                elements.add((new SingleElementMapper(element)).getMyElement());
            } catch (ElementNotFoundException e) {
                // The database must have been updated while making the call.
            }
        }
        try {
            shared.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

    public void closeConnection() {
        gateway.closeConnection();
    }
}
