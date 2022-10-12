package datasource;

import dto.MetalDTO;
import exceptions.GatewayDeletedException;
import exceptions.GatewayNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class AcidDataGateways extends Gateway {
    private String name;
    private long solute;

    /**
     * Constructor that uses the id only to create a row gateway for an existing acid in the DB
     * @param conn Connection to the database
     * @param id Unique identifier for the acid
     */
    public AcidDataGateways(Connection conn, long id) throws GatewayNotFoundException  {
        super();
        this.id = id;
        this.conn = conn;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Acid WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.solute = rs.getLong("solute");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                throw new GatewayNotFoundException("No acid was found with the given id.");
            }
        } catch (SQLException ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor for adding the new acid into the DB and creating a row data gateway for it as well
     * @param conn Connection to the database
     * @param name Name of the acid
     * @param solute ID of the metal that is the solute of this acid
     */
    public AcidDataGateways(Connection conn, String name, long solute) {
        super();
        this.id = KeyTableGateways.getNextValidKey(conn);
        this.name = name;
        this.solute = solute;

        // store the new acid or base in the DB
        try {
            Statement statement = conn.createStatement();
            String addAcid = "INSERT INTO Acid" +
                    "(id, name, solute) VALUES ('" +
                    id + "','" + name + "','" + solute + "')";
            statement.executeUpdate(addAcid);
        } catch (Exception ex) {
            //key didn't insert because already in db?
        }
    }

    /**
     * Validates that the data in the row gateway is valid
     * @return True if the data is valid, false otherwise
     */
    private boolean validate() {
        return (name != null && solute > 0);
    }

    /**
     * Store the current state of the object in the DB
     * @param id ID of the acid to update
     * @param name Name of the acid
     * @param solute ID of the metal that is the solute of this acid
     * @return True if the update was successful, false otherwise
     */
    private boolean persist(long id, String name, long solute) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Acid SET name = '" + name + "', solute = '" + solute +
                    "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            //key didn't insert because already in db?
            return false;
        }
    }

    /**
     * Get the name of the acid
     * @return Name of the acid
     */
    public String getName() throws GatewayDeletedException {
        if (!deleted) {
            return name;
        } else {
            throw new GatewayDeletedException("This acid has been deleted.");
        }
    }

    /**
     * Update the name of the acid
     * @param name New name of the acid
     */
    public void updateName(String name) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, name, this.solute)) this.name = name;
        } else {
            throw new GatewayDeletedException("This acid has been deleted.");
        }
    }

    /**
     * Get the solute of the acid
     * @return ID of the metal that is the solute of this acid
     */
    public long getSolute() throws GatewayDeletedException {
        if (!deleted) {
            return solute;
        } else {
            throw new GatewayDeletedException("This acid has been deleted.");
        }
    }

    /**
     * Update the solute of the acid
     * @param solute ID of the metal that is the solute of this acid
     */
    public void updateSolute(long solute) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, this.name, solute)) this.solute = solute;
        } else {
            throw new GatewayDeletedException("This acid has been deleted.");
        }
    }

    /**
     * Get all the metals that this acid dissolves
     * @return List of Metal DTOs that this acid dissolves
     */
    public ArrayList<MetalDTO> getDissolvedMetals() {
        ArrayList<MetalDTO> metals = new ArrayList<>();

        // Construct our Metal DTOs from the DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Metal WHERE dissolvedBy = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                metals.add(new MetalDTO(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("atomicNumber"),
                        rs.getDouble("atomicMass"),
                        rs.getLong("dissolvedBy")));
            }
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
        return metals;
    }
}
