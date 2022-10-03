package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Both row and table gateways for the element class
 */
public class BaseDataGateways extends Gateway {
    private String name;
    private long solute;
    private long id;

    /**
     * Constructor that uses the id only to create a row gateway for an existing base in the DB
     * @param id
     */
    public BaseDataGateways(long id) {
        super();
        this.id = id;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Base WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            rs.next();
            this.solute = rs.getLong("solute");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                System.out.println("No base was found with the given id.");
            }
        } catch(Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor for adding the new base into the DB and creating a row data gateway for it as well
     */
    public BaseDataGateways(String name, long solute) {
        super();
        this.id = KeyTableGateways.getNextValidKey();
        this.name = name;
        this.solute = solute;

        // store the new base in the DB
        try {
            Statement statement = conn.createStatement();
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
    private boolean validate() {
        return this.id != 0 && this.name != null && this.solute != 0;
    }

    public String getName() {
        if (!deleted) {
            return name;
        }
        else {
            System.out.println("Base is deleted");
        }
        return null;
    }

    public void updateName(String name) throws SQLException {
        if (!deleted) {
            if (persist(this.id, name, solute)) this.name = name;
        } else {
            System.out.println("Base is deleted");
        }
    }

    public long getSolute() {
        if (!deleted) {
            return solute;
        } else {
            System.out.println("Base is deleted");
        }
        return -1;
    }

    public void updateSolute(int solute) {
        if (!deleted) {
            if (persist(this.id, this.name, solute)) this.solute = solute;
        } else {
            System.out.println("Base is deleted");
        }
    }

    public boolean persist(long id, String name, long solute) {
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
