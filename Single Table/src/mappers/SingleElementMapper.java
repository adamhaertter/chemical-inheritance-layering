package mappers;

import datasource.ChemicalDataGateway;
import datasource.Gateway;
import exceptions.ElementNotFoundException;
import model.Element;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleElementMapper extends ElementMapper {
    /**
     * Creates a new entry for the element in the database
     */
    public SingleElementMapper(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);

        Connection conn = Gateway.setUpConnection();
        ChemicalDataGateway chemGW;

        try {
            chemGW = new ChemicalDataGateway(conn, name, atomicNumber, atomicMass,
                    0, 0, 0, "");
            myElement = new Element(name, atomicNumber, atomicMass);
            conn.close();
        } catch (SQLException e) {
        }
    }

    /**
     * Constructor for elements that already exist
     * @param name of the element
     */
    public SingleElementMapper(String name) throws ElementNotFoundException {
        super(name);

        Connection conn = Gateway.setUpConnection();
        ChemicalDataGateway chemGW;
        try {
            chemGW = new ChemicalDataGateway(conn, name);
            myElement = new Element(name, chemGW.getAtomicNumber(), chemGW.getAtomicMass());
            conn.close();
        } catch (SQLException e) {
        }
    }
}
