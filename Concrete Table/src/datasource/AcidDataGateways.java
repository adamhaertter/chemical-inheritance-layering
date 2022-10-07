package datasource;

import dto.MetalDTO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AcidDataGateways extends Gateway {
    private String name;
    private long solute;

    public AcidDataGateways(long id) {
        super();
        this.id = id;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from AcidBase WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.solute = rs.getLong("solute");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.solute = -1;
                System.out.println("No acid or base was found with the given id.");
            }
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    public AcidDataGateways(String name, long solute) {
        super();
        this.id = KeyTableGateways.getNextValidKey();
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

    private boolean validate() {
        return (name != null && solute > 0);
    }

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

    // Getters and setters
    public String getName() {
        if (!deleted) {
            return name;
        } else {
            System.out.println("This acid has been deleted.");
        }
        return null;
    }

    public void updateName(String name) {
        if (!deleted) {
            if (persist(this.id, name, this.solute)) this.name = name;
        } else {
            System.out.println("This acid has been deleted.");
        }
    }

    public long getSolute() {
        if (!deleted) {
            return solute;
        } else {
            System.out.println("This acid has been deleted.");
        }
        return 0;
    }

    public void updateSolute(long solute) {
        if (!deleted) {
            if (persist(this.id, this.name, solute)) this.solute = solute;
        } else {
            System.out.println("This acid has been deleted.");
        }
    }



    // Table gateway to get all metals dissolved by this acid and get the gateways for them
    public ArrayList<MetalDTO> getDissolvedMetals() {
        ArrayList<MetalDTO> metals = new ArrayList<>();

        // Construct our Metal DTOs from the DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Dissolves WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                metals.add(new MetalDTO(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getLong("atomicNumber"),
                        rs.getLong("atomicMass"),
                        rs.getLong("dissolvedBy")));
            }
        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
        return metals;
    }
}
