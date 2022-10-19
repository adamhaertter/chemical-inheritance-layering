package datasource;

import datasource.dto.CompoundToElementDTO;
import datasource.exceptions.GatewayDeletedException;
import datasource.exceptions.GatewayNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class ElementDataGateways extends Gateway {

    private String name;
    private int atomicNumber;
    private double atomicMass;

    /**
     * Constructor for the gateways for existing entries using the id
     * @param conn the connection to the DB
     * @param id the ID of the desired Element
     */
    public ElementDataGateways(Connection conn, long id) throws GatewayNotFoundException {
        super();
        this.id = id;
        this.conn = conn;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Element WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.atomicNumber = rs.getInt("atomicNumber");
            this.atomicMass = rs.getDouble("atomicMass");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.atomicNumber = -1;
                this.atomicMass = -1;
                throw new GatewayNotFoundException("This element was not found");
            }
        } catch (SQLException ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * A constructor to make a row gateway object while also inserting the data into the DB
     * @param conn the connection to the DB
     * @param name the name of the element
     * @param atomicNumber the atomic number of the element
     * @param atomicMass the atomic mass of the element
     */
    public ElementDataGateways(Connection conn, String name, int atomicNumber, double atomicMass) {
        super();
        this.id = KeyTableGateways.getNextValidKey(conn);
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.conn = conn;

        // store the new acid or base in the DB
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO Element" +
                    "(id, name, atomicNumber, atomicMass) VALUES ('" +
                    id + "','" + name + "','" + atomicNumber + "','" + atomicMass + "')");
        } catch (SQLException ex) {
            //key didn't insert because already in db?
        }
    }

    /**
     * Checks to see if our values are valid (if they aren't then something is wrong from the DB
     * @return whether or not our values are valid
     */
    public boolean validate() {
        return name != null && atomicNumber > 0 && atomicMass > 0;
    }

    /**
     * Pushes the data we have currently in the object to the database
     * @param id the id of the element
     * @param name the name of the element
     * @param atomicNumber the atomic number of the element
     * @param atomicMass the atomic mass of the element
     * @return whether the sql query worked
     */
    private boolean persist(long id, String name, int atomicNumber, double atomicMass) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Element SET name = '" + name + "', atomicNumber = '" + atomicNumber +
                    "', atomicMass = '" + atomicMass + "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            //key didn't insert because already in db?
            return false;
        }
    }

    /**
     * Gets the name of the element and returns null if the element is deleted
     * @return the name of the element
     */
    public String getName() throws GatewayDeletedException{
        if (!deleted) {
            return this.name;
        } else {
            throw new GatewayDeletedException("This element is deleted");
        }
    }

    /**
     * Update the name of this element
     * @param name the new name of this element
     */
    public void updateName(String name) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, name, this.atomicNumber, this.atomicMass)) this.name = name;
        } else {
            throw new GatewayDeletedException("This element has been deleted.");
        }
    }

    /**
     * Get the atomic number of this element
     * @return the atomic number of this element
     */
    public int getAtomicNumber() throws GatewayDeletedException{
        if (!deleted) {
            return this.atomicNumber;
        } else {
            throw new GatewayDeletedException("This element is deleted");
        }
    }

    /**
     * Update the atomic number of this element
     * @param atomicNumber the new atomic number
     */
    public void updateAtomicNumber(int atomicNumber) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, this.name, atomicNumber, this.atomicMass)) this.atomicNumber = atomicNumber;
        } else {
            throw new GatewayDeletedException("This element has been deleted.");
        }
    }

    /**
     * Get the atomic mass of this element
     * @return the atomic mass
     */
    public double getAtomicMass() throws GatewayDeletedException{
        if (!deleted) {
            return this.atomicMass;
        } else {
            throw new GatewayDeletedException("This element is deleted");
        }
    }

    /**
     * Update the atomic mass of this element
     * @param atomicMass the new atomic mass
     */
    public void updateAtomicMass(double atomicMass) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, atomicMass)) this.atomicMass = atomicMass;
        } else {
            throw new GatewayDeletedException("This element has been deleted.");
        }
    }

    /**
     * Get all compounds this element is in
     * @return an array list of CompoundToElement DTOs which contain this element
     */
    public static ArrayList<CompoundToElementDTO> getAllCompoundsWithThisElement(Connection conn, long elementId) throws GatewayDeletedException{
        ArrayList<CompoundToElementDTO> compounds = new ArrayList<>();
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from CompoundToElement WHERE elementId = ?");
            statement.setLong(1, elementId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                compounds.add(new CompoundToElementDTO(rs.getLong("compoundId"), rs.getLong("elementId")));
            }
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
        return compounds;
    }


}
