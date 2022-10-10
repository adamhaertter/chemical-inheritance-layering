package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;

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
    }

    protected boolean validate() {
        return this.id != 0 && this.name != null;
    }

    /** getters and setters **/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        verifyExistence();
        this.name = name;
    }
}
