package datasource;

import java.sql.*;

/**
 * Contains both the Row Data Gateway and Table Data Gateway functionality for the Chemical table. Row functions are done
 * by an instance of this class, while the table functions are static methods.
 *
 * As the highest table in the Class Table Inheritance hierarchy, multiple lower classes will extend this class so the
 * rows are managed in all necessary tables.
 */
public class ChemicalDataGateway extends Gateway {

    protected String name = null;

    /**
     * Creates a row data gateway on the Chemical table of the database using an existing id
     * @param conn connection to the DB
     * @param id primary key id for the row
     */
    public ChemicalDataGateway(Connection conn, long id) {
        super(conn);
        this.id = id;
        deleted = false;

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");

            if (!this.validateChemical()) {
                this.id = -1;
                this.name = null;
                System.out.println("No chemical was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new row in the db and a corresponding row data gateway object for it here
     * @param conn connection to the DB
     * @param name the value to put into the name column of the table
     */
    public ChemicalDataGateway(Connection conn, String name) {
        super(conn);
        // Since we are removing inhabits, we don't set that up here
        this.name = name;
        deleted = false;

        // Generate next valid id for this row
        id = KeyTableGateway.getNextValidKey(conn);

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Chemical" +
                    "(id, name) VALUES ('" +
                    id + "','" + name + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks the validity of the information in the row.
     *
     * @return Whether the current columns for this row have valid values
     */
    protected boolean validateChemical() {
        boolean test1 = this.id != 0;
        boolean test2 = this.name != null;
        boolean test3 = test1 && test2;
        return  test3;
    }

    /**
     * Updates the database with the values stored to the instance variables of the gateway.
     * @return Whether the update is passed correctly.
     */
    public boolean persist(long id, String name) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE Chemical SET name = '" + name +
                    "' WHERE id = '" + id + "'");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * If the entry is deleted, prints a message. Returns whether it has been deleted.
     * @return Whether the entry exists still.
     */
    public boolean verify() {
        if(deleted) {
            System.out.println("Entry " + name + " has been deleted.");
        }
        return !deleted;
    }


    /** getters and setters **/

    /**
     * Checks that the row is valid, then returns the name of the Chemical
     * @return name of the chemical, null if invalid
     */
    public String getName() {
        if(verify())
            return name;
        else
            return null;
    }

    /**
     * Updates the chemical's name in the database. A message will be printed if this does not occur.
     *
     * @param name the name with which to update the DB
     */
    public void updateName(String name) {
        // This basic format should be used for all lower setters:
        // If the row exists AND we can update the values in the DB...
        if( verify() && persist(this.id, name) )
            this.name = name;
        // Set the instance variable to match
    }

    /**
     * Given a name, return the id that links all the subtables together. Queries the database based on name.
     *
     * @param name The unique name of the Chemical, stored in this table and no others by our implementation.
     * @return the id shared by all instances of this Chemical in any subtables.
     */
    public static long getIdByName(Connection conn, String name) {
        try{
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE name = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            long id = rs.getLong("id");
            return id;
        } catch (SQLException e) {
            return -1;
        }
    }
}
