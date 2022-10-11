package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ElementDataGateway extends ChemicalDataGateway {

    private int atomicNumber = 0;
    private double atomicMass = 0;

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

    protected boolean validate() {
        return super.validate() && this.atomicNumber > 0 && this.atomicMass > 0.0;
    }

    /**
     * Updates the database with the values stored to the instance variables of the gateway.
     * Cascades upward to all parent tables.
     * @return Whether the update is passed correctly.
     */
    protected boolean persist() {
        super.persist();
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
    public int getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        if( !verify() )
            return;
        this.atomicNumber = atomicNumber;
        persist();
    }

    public double getAtomicMass() {
        return atomicMass;
    }

    public void setAtomicMass(double atomicMass) {
        if( !verify() )
            return;
        this.atomicMass = atomicMass;
        persist();
    }
}
