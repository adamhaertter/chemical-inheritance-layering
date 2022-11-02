package mappers;

import datasource.ChemicalDataGateway;
import datasource.ElementDataGateway;
import datasource.Gateway;
import exceptions.ElementNotFoundException;
import model.Element;

import java.sql.Connection;
import java.sql.SQLException;

public class ClassElementMapper extends ElementMapper {

    /**
     * Create a new element in the database, and store the resulting model object
     * into my instance variable
     */
    public ClassElementMapper(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);

        Connection conn = Gateway.setUpConnection();
        new ElementDataGateway(conn, name, atomicNumber, atomicMass);

        myElement = new Element(name, atomicNumber, atomicMass);

        try {
            assert conn != null;
            conn.close();
        } catch (SQLException ignored) {
        }
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
        ElementDataGateway gateway = new ElementDataGateway(conn, myId);

        myElement = new Element(name, gateway.getAtomicNumber(), gateway.getAtomicMass());

        try {
            conn.close();
        } catch (SQLException e) {
        }
    }
}
