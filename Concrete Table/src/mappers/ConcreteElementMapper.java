package mappers;

import datasource.ElementDataGateways;
import datasource.Gateway;
import datasource.enums.TableEnums;
import datasource.exceptions.GatewayDeletedException;
import datasource.exceptions.GatewayFailedToDelete;
import datasource.exceptions.GatewayNotFoundException;
import exceptions.ElementNotFoundException;
import model.Element;

public class ConcreteElementMapper extends ElementMapper {

    private ElementDataGateways gateway;

    /**
     * Create a new element in the database, and store the resulting model object
     * into my instance variable
     */
    public ConcreteElementMapper(String name, int atomicNumber, double atomicMass) {
        super(name, atomicNumber, atomicMass);

        conn = Gateway.setUpConnection();
        this.gateway = new ElementDataGateways(conn, name, atomicNumber, atomicMass);

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
    public ConcreteElementMapper(String name) throws ElementNotFoundException {
        super(name);
        conn = Gateway.setUpConnection();

        try {
            assert conn != null;
            this.gateway = new ElementDataGateways(conn, name);

            myElement = new Element(this, name, gateway.getAtomicNumber(), gateway.getAtomicMass());
        } catch (GatewayNotFoundException | GatewayDeletedException e) {
            throw new ElementNotFoundException("No element found with name " + name);
        }
    }

    public void persists(String name, int atomicNumber, double atomicMass) {
        this.gateway.persist(this.gateway.getId(), name, atomicNumber, atomicMass);
    }

    public void delete()
    {
        try {
            gateway.delete(TableEnums.Table.Element);
        } catch (GatewayFailedToDelete e) {
            System.out.println("Failed to delete from the Element table");
        }
    }
}
