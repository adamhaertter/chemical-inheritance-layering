package datasource;

import dto.CompoundToElementDTO;
import exceptions.GatewayDeletedException;
import exceptions.GatewayNotFoundException;

import java.sql.*;
import java.util.ArrayList;


/**
 * Both row and table gateways for the element class
 */
public class CompoundDataGateways extends Gateway {
    private String name;

    /**
     * Constructor that uses the id only to create a row gateway for an existing compound in the DB
     *
     * @param conn our connection to the DB
     * @param id   the id of the desired compound
     */
    public CompoundDataGateways(Connection conn, long id) throws GatewayNotFoundException {
        super();
        this.id = id;
        this.conn = conn;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Compound WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                throw new GatewayNotFoundException("No compound was found with the given id.");
            }
        } catch (SQLException ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor for adding the new compound into the DB and creating a row data gateway for it as well
     *
     * @param conn connection for the DB
     * @param name the name of the compound we want
     */
    public CompoundDataGateways(Connection conn, String name) {
        super();
        this.id = KeyTableGateways.getNextValidKey(conn);
        this.name = name;
        this.conn = conn;

        // store the new compound in the DB
        try {
            Statement statement = this.conn.createStatement();
            String addCompound = "INSERT INTO Compound" +
                    "(id, name) VALUES ('" +
                    id + "','" + name + "')";
            statement.executeUpdate(addCompound);
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Validates that the data in the row gateway is valid
     *
     * @return true if the data is valid, false otherwise
     */
    public boolean validate() {
        return (this.name != null);
    }

    /**
     * Getter for the name of the compound
     *
     * @return the name of the compound
     */
    public String getName() throws GatewayDeletedException {
        if (!deleted) {
            return this.name;
        } else {
            throw new GatewayDeletedException("This compound has been deleted.");
        }
    }

    /**
     * Updates the name of the current compound in our gateway and the DB
     *
     * @param name the new name of the compound
     */
    public void updateName(String name) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, name)) this.name = name;
        } else {
            throw new GatewayDeletedException("This compound has been deleted.");
        }
    }

    /**
     * Pushes whatever changes we give to the DB
     *
     * @param id   the id of the compound we want to update
     * @param name the new name of the compound
     * @return True if the update was successful, false otherwise
     */
    private boolean persist(long id, String name) {
        try {
            Statement statement = this.conn.createStatement();
            statement.executeUpdate("UPDATE Compound SET name = '" + name + "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Add a new CompoundToElement relationship to the DB
     *
     * @param elementId the id of the element we want to add to this compound
     */
    public void addElement(long elementId) {
        try {
            Statement statement = this.conn.createStatement();
            String addElement = "INSERT INTO CompoundToElement" +
                    "(compoundId, elementId) VALUES ('" +
                    this.id + "','" + elementId + "')";
            statement.executeUpdate(addElement);
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Returns all the elements within this compound
     *
     * @return an array list of the elements within this compound in the form of DTOs
     */
    public static ArrayList<CompoundToElementDTO> getAllElementsInCompound(Connection conn, long compoundId) {
        ArrayList<CompoundToElementDTO> elements = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM CompoundToElement WHERE compoundId = '" + compoundId + "'");
            while (rs.next()) {
                elements.add(new CompoundToElementDTO(rs.getLong("compoundId"), rs.getLong("elementId")));
            }
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
        return elements;
    }
}
