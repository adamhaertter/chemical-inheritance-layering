package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChemicalDataGateway extends Gateway {

    protected String name = "";

    /**
     * Creates a row data gateway on the Chemical table of the database using an existing id
     * @param id primary key id for the row
     */
    public ChemicalDataGateway(long id) {
        super();
        this.id = id;
        deleted = false;

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Chemical WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");

            if (!validate()) {
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
     * @param name the value to put into the name column of the table
     */
    public ChemicalDataGateway(String name) {
        super();
        // Since we are removing inhabits, we don't set that up here
        this.name = name;
        deleted = false;

        //TODO Set up id for all children

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

    protected boolean validate() {
        return this.id != 0 && this.name != null;
    }

    /**
     * Updates the database with the values stored to the instance variables of the gateway.
     * @return Whether the update is passed correctly.
     */
    protected boolean persist() {
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if( !verify() )
            return;
        this.name = name;
        persist();
    }
}
