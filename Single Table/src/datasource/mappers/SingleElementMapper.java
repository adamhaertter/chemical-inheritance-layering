package datasource.mappers;

import datasource.ChemicalDataGateway;
import datasource.Gateway;
import exceptions.ElementNotFoundException;
import mappers.ElementMapper;
import model.Element;

import java.sql.Connection;

public class SingleElementMapper extends ElementMapper {
    ChemicalDataGateway chemGW;

    public SingleElementMapper(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);

        Connection conn = Gateway.setUpConnection();
        try {
            chemGW = new ChemicalDataGateway(conn, name);
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        myElement = new Element(name);
    }

    public SingleElementMapper(String name) throws ElementNotFoundException {
        super(name);

        Connection conn = Gateway.setUpConnection();
        try {
            chemGW = new ChemicalDataGateway(conn, name);
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int atomicNumber = chemGW.getAtomicNumber();
        double atomicMass = chemGW.getAtomicMass();
        myElement = new Element(name, atomicNumber, atomicMass);
    }
}
