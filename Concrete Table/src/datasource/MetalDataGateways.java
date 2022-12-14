package datasource;

import datasource.exceptions.GatewayDeletedException;
import datasource.exceptions.GatewayNotFoundException;

import java.sql.*;

public class MetalDataGateways extends Gateway {
    private String name;
    private int atomicNumber;
    private double atomicMass;
    private long dissolvedBy;

    /**
     * Constructor that uses the id only to create a row gateway for an existing base in the DB
     * @param id Unique identifier for the metal
     */
    public MetalDataGateways(Connection conn, long id) throws GatewayNotFoundException {
        super();
        this.id = id;
        this.conn = conn;
        try {
            CallableStatement statement = this.conn.prepareCall("SELECT * from Metal WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.atomicNumber = rs.getInt("atomicNumber");
            this.atomicMass = rs.getDouble("atomicMass");
            this.dissolvedBy = rs.getLong("dissolvedBy");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.atomicNumber = -1;
                this.atomicMass = -1;
                this.dissolvedBy = -1;
                throw new GatewayNotFoundException("This metal was not found");
            }

        } catch (SQLException ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor for adding the new base into the DB and creating a row data gateway for it as well
     * @param conn Connection to the database
     * @param name Name of the metal
     * @param atomicNumber Atomic number of the metal
     * @param atomicMass Atomic mass of the metal
     * @param dissolvedBy ID of the acid that dissolves this metal
     */
    public MetalDataGateways(Connection conn, String name, int atomicNumber, double atomicMass, long dissolvedBy) {
        super();
        this.id = KeyTableGateways.getNextValidKey(conn);
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.dissolvedBy = dissolvedBy;
        this.conn = conn;

        // store the new base in the DB
        try {
            Statement statement = conn.createStatement();
            String addMetal = "INSERT INTO Metal" +
                    "(id, name, atomicNumber, atomicMass, dissolvedBy) VALUES ('" +
                    id + "','" + name + "','" + atomicNumber + "','" + atomicMass + "','" + dissolvedBy + "')";
            statement.executeUpdate(addMetal);
        } catch (SQLException ex) {
            //key didn't insert because already in db?
        }
    }

    /**
     * This method checks if everything we need for this object exists
     * @return True if the data is valid, false otherwise
     */
    public boolean validate() {
        return (name != null && atomicNumber > 0 && atomicMass > 0);
    }

    /**
     * Store the current state of the object in the DB
     * @param id Unique identifier for the metal
     * @param name Name of the metal
     * @param atomicNumber Atomic number of the metal
     * @param atomicMass Atomic mass of the metal
     * @param dissolvedBy ID of the acid that dissolves this metal
     * @return True if the update was successful, false otherwise
     */
    private boolean persist(long id, String name, int atomicNumber, double atomicMass, long dissolvedBy) {
        try {
            Statement statement = conn.createStatement();
            String q = "UPDATE Metal SET name = '" + name + "', atomicNumber = '" + atomicNumber + "', atomicMass = '" + atomicMass + "', dissolvedBy = '" + dissolvedBy + "' WHERE id = '" + id + "'";
            statement.executeUpdate(q);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Get the name of the metal
     * @return Name of the metal
     */
    public String getName() throws GatewayDeletedException {
        if (!deleted) {
            return name;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Update the name of the metal
     * @param name New name of the metal
     */
    public void updateName(String name) throws GatewayDeletedException {
        if (!deleted) {
            boolean test = persist(this.id, name, this.atomicNumber, this.atomicMass, this.dissolvedBy);
            if (test) this.name = name;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Get the atomic number of the metal
     * @return Atomic number of the metal
     */
    public long getAtomicNumber() throws GatewayDeletedException {
        if (!deleted) {
            return atomicNumber;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Update the atomic number of the metal
     * @param atomicNumber New atomic number of the metal
     */
    public void updateAtomicNumber(int atomicNumber) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, this.name, atomicNumber, this.atomicMass, this.dissolvedBy))
                this.atomicNumber = atomicNumber;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Get the atomic mass of the metal
     * @return Atomic mass of the metal
     */
    public double getAtomicMass() throws GatewayDeletedException {
        if (!deleted) {
            return atomicMass;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Update the atomic mass of the metal
     * @param atomicMass New atomic mass of the metal
     */
    public void updateAtomicMass(double atomicMass) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, atomicMass, this.dissolvedBy))
                this.atomicMass = atomicMass;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Get the ID of the acid that dissolves this metal
     * @return ID of the acid that dissolves this metal
     */
    public long getDissolvedBy() throws GatewayDeletedException {
        if (!deleted) {
            return dissolvedBy;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

    /**
     * Update the acid that dissolves this metal
     * @param dissolvedBy New ID of the acid that dissolves this metal
     */
    public void updateDissolvedBy(long dissolvedBy) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, this.atomicMass, dissolvedBy))
                this.dissolvedBy = dissolvedBy;
        } else {
            throw new GatewayDeletedException("The metal has been deleted");
        }
    }

}
