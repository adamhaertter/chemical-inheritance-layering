package mappers;

import datasource.ChemicalDataGateway;
import datasource.CompoundDataGateway;
import datasource.CompoundToElementDataGateway;
import datasource.Gateway;
import exceptions.CompoundNotFoundException;
import model.Compound;

import java.sql.Connection;
import java.sql.SQLException;

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
    public void addElement(String name) {
        Connection conn = Gateway.setUpConnection();
        long toAdd = ChemicalDataGateway.getIdByName(conn, name);
        long compoundId = gateway.getId();
        new CompoundToElementDataGateway(conn, compoundId, toAdd);
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
