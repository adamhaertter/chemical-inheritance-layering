package mappers;

import datasource.*;
import exceptions.CompoundNotFoundException;
import exceptions.ElementNotFoundException;
import model.Compound;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassCompoundMapper extends CompoundMapper {

    private CompoundDataGateway gateway;

    /**
     * Constructor for objects that exist in the db
     *
     * @param name
     */
    public ClassCompoundMapper(String name) throws CompoundNotFoundException {
        super(name);

        Connection conn = Gateway.setUpConnection();
        long myId = ChemicalDataGateway.getIdByName(conn, name);
        if(myId == -1) {
            throw new CompoundNotFoundException("No compound found with name " + name);
        }
        this.gateway = new CompoundDataGateway(conn, myId);

        myCompound = new Compound(this, name);

    }

    /**
     * Create a new compound in the database, and store the resulting model object
     * into my instance variable
     */
    public static void createCompound(String name) {
        Connection conn = Gateway.setUpConnection();
        CompoundDataGateway gateway = new CompoundDataGateway(conn, name);
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the database with the given information by calling upon the Gateway
     * @param name name of the compound
     */
    @Override
    public void persists(String name) {
        this.gateway.persist(gateway.getId(), name);
    }

    /**
     * Deletes the row from the database
     */
    @Override
    public void delete() {
        this.gateway.delete();
    }

    @Override
    public void addElement(String name) throws ElementNotFoundException {
        Connection conn = Gateway.setUpConnection();
        long toAdd = ChemicalDataGateway.getIdByName(conn, name);
        long compoundId = gateway.getId();
        CompoundToElementDataGateway.createCompoundElementPair(conn, compoundId, toAdd);
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getElementsInCompound() {
        ArrayList elementNames = new ArrayList();
        long id = gateway.getId();
        ArrayList<Long> elementIds = CompoundToElementDataGateway.getElementsInCompound(id);
        Connection conn2 = Gateway.setUpConnection();
        for(long element : elementIds) {
            elementNames.add((new ElementDataGateway(conn2, element)).getName());
        }
        try {
            conn2.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elementNames;
    }
}
