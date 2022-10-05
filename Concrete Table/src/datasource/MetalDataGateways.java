package datasource;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MetalDataGateways extends Gateway {
    private String name;
    private long atomicNumber;
    private long atomicMass;
    private long dissolvedBy;

    /**
     * Constructor that uses the id only to create a row gateway for an existing base in the DB
     *
     * @param id
     */
    public MetalDataGateways(long id) {
        super();
        this.id = id;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Metal WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.name = rs.getString("name");
            this.atomicNumber = rs.getLong("atomicNumber");
            this.atomicMass = rs.getLong("atomicMass");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.atomicNumber = -1;
                this.atomicMass = -1;
                System.out.println("No metal was found with the given id.");
            }

        } catch (Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor for adding the new base into the DB and creating a row data gateway for it as well
     */
    public MetalDataGateways(String name, long atomicNumber, long atomicMass, long dissolvedBy) {
        super();
        this.id = KeyTableGateways.getNextValidKey();
        this.name = name;
        this.atomicNumber = atomicNumber;
        this.atomicMass = atomicMass;
        this.dissolvedBy = dissolvedBy;

        // store the new base in the DB
        try {
            Statement statement = conn.createStatement();
            String addMetal = "INSERT INTO Metal" +
                    "(id, name, atomicNumber, atomicMass, dissolvedBy) VALUES ('" +
                    id + "','" + name + "','" + atomicNumber + "','" + atomicMass + "','" + dissolvedBy + "')";
            statement.executeUpdate(addMetal);
        } catch (Exception ex) {
            //key didn't insert because already in db?
        }
    }

    public boolean validate() {
        return (name != null && atomicNumber > 0 && atomicMass > 0);
    }

    public boolean persist(long id, String name, long atomicNumber, long atomicMass, long dissolvedBy) {
        try {
            Statement statement = conn.createStatement();
            String updateMetal = "UPDATE Metal SET name = '" + name + "', atomicNumber = '" + atomicNumber + "', atomicMass = '" + atomicMass + "', dissolvedBy = '" + dissolvedBy + "' WHERE id = '" + id + "'";
            statement.executeUpdate(updateMetal);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getName() {
        if (!deleted) {
            return name;
        } else {
            System.out.println("This metal has been deleted.");
        }
        return null;
    }

    public void setName(String name) {
        if (!deleted) {
            if (persist(this.id, name, this.atomicNumber, this.atomicMass, this.dissolvedBy)) this.name = name;
        } else {
            System.out.println("This metal has been deleted.");
        }
    }

    public long getAtomicNumber() {
        if (!deleted) {
            return atomicNumber;
        } else {
            System.out.println("This metal has been deleted.");
        }
        return -1;
    }

    public void setAtomicNumber(long atomicNumber) {
        if (!deleted) {
            if (persist(this.id, this.name, atomicNumber, this.atomicMass, this.dissolvedBy))
                this.atomicNumber = atomicNumber;
        } else {
            System.out.println("This metal has been deleted.");
        }
    }

    public long getAtomicMass() {
        if (!deleted) {
            return atomicMass;
        } else {
            System.out.println("This metal has been deleted.");
        }
        return -1;
    }

    public void setAtomicMass(long atomicMass) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, atomicMass, this.dissolvedBy))
                this.atomicMass = atomicMass;
        } else {
            System.out.println("This metal has been deleted.");
        }
    }

    public long getDissolvedBy() {
        if (!deleted) {
            return dissolvedBy;
        } else {
            System.out.println("This metal has been deleted.");
        }
        return -1;
    }

    public void setDissolvedBy(long dissolvedBy) {
        if (!deleted) {
            if (persist(this.id, this.name, this.atomicNumber, this.atomicMass, dissolvedBy))
                this.dissolvedBy = dissolvedBy;
        } else {
            System.out.println("This metal has been deleted.");
        }
    }

}
