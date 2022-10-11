package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Contains both the Row Data Gateway and Table Data Gateway functionality for the Element table. Row functions are done
 * by an instance of this class, while the table functions are static methods.
 *
 * Extends the ChemicalDataGateway class so the implementation works across the multiple inherited tables for
 * Class Table Inheritance.
 */
public class ElementDataGateway extends ChemicalDataGateway {

    protected int atomicNumber = 0;
    protected double atomicMass = 0;

    /**
     * Reads a row corresponding to some Element id and creates a row data gateway from it
     * @param id the primary key id of Element
     */
    public ElementDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Element WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.atomicNumber = rs.getInt("atomicNumber");
            this.atomicMass = rs.getDouble("atomicMass");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.atomicMass = -1.0;
                this.atomicNumber = -1;
                System.out.println("No element was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new row in the database and a corresponding row data gateway for it
     * @param name the name field of the parent table Chemical
     * @param atomicNumber the atomic number field of Element
     * @param atomicMass the atomic mass field of Element
     */
    public ElementDataGateway(String name, int atomicNumber, double atomicMass) {
        super(name);
        // Since we are removing inhabits, we don't set that up here
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        deleted = false;

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Element" +
                    "(id, atomicNumber, atomicMass) VALUES ('" +
                    id + "','" + atomicNumber +  "','" + atomicMass + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Makes sure the variables of atomicNumber and atomicMass have valid numbers given to them.
     * @return True if the row is valid, false otherwise
     */
    protected boolean validate() {
        return super.validate() && this.atomicNumber > 0 && this.atomicMass > 0.0;
    }

    /**
     * Updates the database with the values stored to the instance variables of the gateway.
     * Cascades upward to all parent tables.
     * @return Whether the update is passed correctly.
     */
    protected boolean persist(long id, String name, double atomicMass, int atomicNumber) {
        super.persist(id, name);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Element SET atomicMass = '" + atomicMass + "', atomicNumber = '" + atomicNumber +
                    "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    /** getters and setters **/

    /**
     * Checks that the row is valid, then returns atomic number
     * @return value of the atomic number, -1 if invalid
     */
    public int getAtomicNumber() {
        if(verify())
            return atomicNumber;
        else
            return -1;
    }

    /**
     * Updates the atomic number in the database. A message will be printed if this does not occur.
     *
     * @param atomicNumber the atomic number value with which to update the DB
     * @see ChemicalDataGateway#setName(String) for line-by-line comments on structure
     */
    public void updateAtomicNumber(int atomicNumber) {
        if( !verify() && !persist(this.id, this.name, this.atomicMass, atomicNumber) )
            return;
        this.atomicNumber = atomicNumber;
    }

    /**
     * Checks that the row is valid, then returns atomic mass
     * @return value of the atomic mass, -1 if invalid
     */
    public double getAtomicMass() {
        if(verify())
            return atomicMass;
        else
            return -1;
    }

    /**
     * Updates the atomic mass in the database. A message will be printed if this does not occur.
     *
     * @param atomicMass the atomic mass value with which to update the DB
     * @see ChemicalDataGateway#setName(String) for line-by-line comments
     */
    public void updateAtomicMass(double atomicMass) {
        if( !verify() && !persist(this.id, this.name, atomicMass, this.atomicNumber) )
            return;
        this.atomicMass = atomicMass;
    }
}
