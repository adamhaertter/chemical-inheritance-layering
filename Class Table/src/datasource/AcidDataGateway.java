package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AcidDataGateway extends ChemicalDataGateway {

    private long solute = 0;
    private ArrayList<Long> dissolves;

    /**
     * Used to create a new row data gateway for an existing acid in the database
     * @param id The id primary key in the db
     */
    public AcidDataGateway(long id) {
        super(id);
        this.id = id;
        deleted = false;

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Acid WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.solute = rs.getLong("solute");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                System.out.println("No acid was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dissolves = MetalDataGateway.getAllDissolvedBy(id);
    }

    /**
     * Used to create a new acid in the database and a row data gateway for it
     * @param name the name field of the parent table, Chemical
     * @param solute the solute field of the db
     */
    public AcidDataGateway(String name, long solute) {
        super(name);
        this.solute = solute;
        deleted = false;

        // Create in DB
        try {
            Statement statement = conn.createStatement();
            String addEntry = "INSERT INTO Acid" +
                    "(id, solute) VALUES ('" +
                    id + "','" + solute + "')";
            statement.executeUpdate(addEntry);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dissolves = MetalDataGateway.getAllDissolvedBy(id);
    }

    protected boolean validate() {
        return super.validate() && this.solute != 0;
    }

    /** getters and setters **/
    public long getSolute() {
        return solute;
    }

    public void setSolute(long solute) {
        verifyExistence();
        this.solute = solute;
    }

    /**
     * @return metal ids dissolved by this Acid
     */
    public ArrayList<Long> getDissolves() {
        return dissolves;
    }
}
