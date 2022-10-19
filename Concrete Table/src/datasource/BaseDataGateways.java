package datasource;

import datasource.exceptions.GatewayDeletedException;
import datasource.exceptions.GatewayNotFoundException;
import datasource.exceptions.SoluteDoesNotExist;
import datasource.utils.ValidationUtils;

import java.sql.*;

/**
 * Both row and table gateways for the element class
 */
public class BaseDataGateways extends Gateway {
    private String name;
    private long solute;

    /**
     * Constructor that uses the id only to create a row gateway for an existing base in the DB
     * @param conn our connection to the DB
     * @param id the id of the desired base
     */
    public BaseDataGateways(Connection conn, long id) throws GatewayNotFoundException {
        super();
        this.id = id;
        this.conn = conn;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Base WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.solute = rs.getLong("solute");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                throw new GatewayNotFoundException("No base was found with the given id.");
            }
        } catch(SQLException ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor for adding the new base into the DB and creating a row data gateway for it as well
     * @param conn connection for the DB
     * @param name the name of the base we want
     * @param solute the solute of the base we want
     */
    public BaseDataGateways(Connection conn, String name, long solute) throws SoluteDoesNotExist {
        super();
        this.id = KeyTableGateways.getNextValidKey(conn);
        this.name = name;
        this.solute = solute;
        this.conn = conn;

        if (!ValidationUtils.doesSoluteExist(conn, solute)) {
            this.id = -1;
            this.name = null;
            this.solute = -1;
            throw new SoluteDoesNotExist("The solute does not exist in the database, in violation of the foreign key constraint.");
        }

        // store the new base in the DB
        try {
            Statement statement = this.conn.createStatement();
            String addBase = "INSERT INTO Base" +
                    "(id, name, solute) VALUES ('" +
                    id + "','" + name + "','" + solute + "')";
            statement.executeUpdate(addBase);
        } catch (Exception ex) {
            //key didn't insert because already in db?
        }
    }

    /**
     * This method checks if everything we need for this object exists, some gateways may be able to have some fields
     * empty, for this example it is not the case
     * @return if this instance is valid
     */
    private boolean validate() { return this.id > 0 && this.name != null && this.solute > 0; }

    /**
     * Getter for the name of the base
     * @return the name of the base
     */
    public String getName() throws GatewayDeletedException {
        if (!deleted) {
            return name;
        }
        else {
            throw new GatewayDeletedException("This base has been deleted.");
        }
    }

    /**
     * Updates the name of the current base in our gateway and the DB
     * @param name the new name of the base
     */
    public void updateName(String name) throws GatewayDeletedException {
        if (!deleted) {
            if (persist(this.id, name, solute)) this.name = name;
        } else {
            throw new GatewayDeletedException("This base has been deleted.");
        }
    }

    /**
     * Gets the name of the base
     * @return the solute of the base
     */
    public long getSolute() throws GatewayDeletedException {
        if (!deleted) {
            return solute;
        } else {
            throw new GatewayDeletedException("This base has been deleted.");
        }
    }

    /**
     * Updates the solute of the current base in our gateway and the DB
     * @param solute the new solute of the base
     */
    public void updateSolute(int solute) throws GatewayDeletedException, SoluteDoesNotExist {
        if (!deleted) {
            if (ValidationUtils.doesSoluteExist(conn, solute)) {
                if (persist(this.id, name, solute)) this.solute = solute;
            } else {
                throw new SoluteDoesNotExist("The solute does not exist in the database, in violation of the foreign key constraint.");
            }
        } else {
            throw new GatewayDeletedException("This base has been deleted.");
        }
    }

    /**
     * Pushes whatever changes we give it to the DB
     * @param id the id of the base we want to update
     * @param name the name of the base we want to update
     * @param solute the solute of the base we want to update
     * @return True if the update was successful, false otherwise
     */
    private boolean persist(long id, String name, long solute) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Base SET name = '" + name + "', solute = '" + solute +
                    "' WHERE id = '" + id + "'");
        } catch (Exception ex) {
            // Fails because already exists?
            return false;
        }
        return true;
    }
}
