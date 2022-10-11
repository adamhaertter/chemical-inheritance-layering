package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CompoundDataGateway extends ChemicalDataGateway {

    /**
     * Used to create a new row data gateway for an existing compound in the database
     * @param id The id primary key in the db
     */
    public CompoundDataGateway(long id) {
        super(id);

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Compound WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();

            if (!validate()) {
                this.id = -1;
                this.name = null;
                System.out.println("No compound was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Used to create a new compound in the database and a row data gateway for it
     * @param name the name field of the parent table, Chemical
     */
    public CompoundDataGateway(String name) {
        super(name);

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Compound" +
                    "(id) VALUES ('" +
                    id + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
