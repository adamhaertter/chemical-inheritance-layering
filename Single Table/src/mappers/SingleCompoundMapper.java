package mappers;

import datasource.ChemicalDataGateway;
import datasource.CompoundDataGateway;
import datasource.Gateway;
import exceptions.CompoundNotFoundException;
import exceptions.ElementNotFoundException;
import model.Compound;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SingleCompoundMapper extends CompoundMapper {
    private ChemicalDataGateway gateway;

    /**
     * Constructor for objects that exist in the database
     * @param name
     * @throws CompoundNotFoundException
     */
    public SingleCompoundMapper(String name) throws CompoundNotFoundException {
        super(name);

        Connection conn = Gateway.setUpConnection();
        long myId = ChemicalDataGateway.getIdByName(conn, name);
        if(myId == -1) {
            throw new CompoundNotFoundException("No compound found with name " + name);
        }
        this.gateway = new ChemicalDataGateway(conn, myId);
        this.gateway.updateType("Compound");

        myCompound = new Compound(this, name);
    }

    /**
     * Create a new compound in the database, and store the resulting model object
     * into my instance variable
     * @param name
     */
    public static void createCompound(String name) {
        Connection conn = Gateway.setUpConnection();
        ChemicalDataGateway chemGW = new
                ChemicalDataGateway(conn, name);
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add an element to the database
     * @param name
     * @throws ElementNotFoundException
     */
    public void addElement(String name) throws ElementNotFoundException {
        Connection conn = Gateway.setUpConnection();
        long compoundID = gateway.getId();
        long elemID = gateway.getIdByName(conn, name);
        CompoundDataGateway compGW = new CompoundDataGateway(conn, compoundID, elemID);
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the database
     * @param name
     */
    public void persists(String name) {
        this.gateway.persist(gateway.getId(), name, gateway.getAtomicNumber(), gateway.getAtomicMass(),
                gateway.getAcidSolute(), gateway.getBaseSolute(), gateway.getDissolvedBy(), gateway.getType());
    }

    /**
     * Deletes the row from the database
     */
    @Override
    public void delete() {
        this.gateway.delete();
    }

    /**
     * Gets the elements in a compound
     * @return elementNames - a list of the element names in a compound
     */
    @Override
    public ArrayList<String> getElementsInCompound() {
        ArrayList elementNames = new ArrayList();
        long id = gateway.getId();
        Connection conn = Gateway.setUpConnection();
        ArrayList<Long> elementIds = CompoundDataGateway.getElementsInCompound(id);

        for(long element : elementIds) {
            elementNames.add((new ChemicalDataGateway(conn, element)).getName());
        }
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elementNames;
    }
}
