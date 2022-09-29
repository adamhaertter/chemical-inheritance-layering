import config.ProjectConfig;
import utils.DatabaseMethods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Both row and table gateways for the element class
 */
public class BaseDataGateways extends Gateway {
    private String name;
    private long solute;
    private long id;
    private DatabaseMethods dbMethods = new DatabaseMethods();

    /**
     * Constructor that uses the id only to create a row gateway for an existing base in the DB
     * @param id
     */
    public BaseDataGateways(long id) {
        this.id = id;
        //query DB for base with the given id
    }

    /**
     * Constructor for adding the new base into the DB and creating a row data gateway for it as well
     */
    public BaseDataGateways(String name, long solute) {
        this.name = name;
        this.solute = solute;
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
            dbMethods.updateBase(id, name, solute);
        } catch (Exception ex) {
            // Fails because already exists?
            return false;
        }
        return true;
    }
}
