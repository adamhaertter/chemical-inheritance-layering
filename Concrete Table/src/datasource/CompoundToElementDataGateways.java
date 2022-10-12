package datasource;

import exceptions.GatewayDeletedException;
import exceptions.GatewayNotFoundException;

import java.sql.*;

public class CompoundToElementDataGateways extends Gateway {

    private long compoundId;
    private long elementId;

    /**
     * Constructor for getting a CompoundToElement row gateway for an existing entry in the DB
     * @param conn Connection to the DB
     * @param compoundId ID of the compound
     * @param elementId ID of the element
     */
    public CompoundToElementDataGateways(Connection conn, long compoundId, long elementId) throws GatewayNotFoundException {
        super();
        this.compoundId = compoundId;
        this.elementId = elementId;
        this.conn = conn;

        // Get the Compound to Element row from the DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from CompoundToElement WHERE compoundId = ? AND elementId = ?");
            statement.setLong(1, compoundId);
            statement.setLong(2, elementId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.compoundId = rs.getLong("compoundId");
            this.elementId = rs.getLong("elementId");

            if (!validate()) {
                this.compoundId = -1;
                this.elementId = -1;
                throw new GatewayNotFoundException("No compound to element was found with the given compoundId and elementId.");
            }
        } catch (SQLException ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Validates that the data in the row gateway is valid
     * @return true if the data is valid, false otherwise
     */
    public boolean validate() {
        return compoundId > 0 && elementId > 0;
    }

    /**
     * Get the compoundId of this compound to element relation
     * @return id of the compound
     */
    public long getCompoundId() throws GatewayDeletedException {
        if (!deleted) {
            return this.compoundId;
        } else {
            throw new GatewayDeletedException("This compound to element relation has been deleted.");
        }
    }

    /**
     * Get the elementId of this compound to element relation
     * @return id of the element
     */
    public long getElementId() throws GatewayDeletedException {
        if (!deleted) {
            return this.elementId;
        } else {
            throw new GatewayDeletedException("This compound to element relation has been deleted.");
        }
    }

    /**
     * Deletes the compound to element relation from the database.
     */
    public void delete() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM CompoundToElement WHERE compoundId = " + compoundId + " AND elementId = " + elementId);
        } catch (Exception ex) {
            //key didn't insert because already in db?
        }
    }
}
