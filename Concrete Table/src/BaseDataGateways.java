import java.util.*;

/**
 * Both row and table gateways for the element class
 */
public class BaseDataGateways extends Gateway {
    private String name;
    private int solute;

    /**
     * Constructor that uses the id only to create a row gateway for an existing base in the DB
     * @param id
     */
    public BaseDataGateways(long id) {
        //query DB for base with the given id
    }

    /**
     * Constructor for adding the new base into the DB and creating a row data gateway for it as well
     */
    public BaseDataGateways(String name, int solute) {
        this.name = name;
        this.solute = solute;
        // put into DB
    }

    public String getName() {
        if (!deleted) {
            return name;
        }
        else {
            //throw error here instead of returning null
        }
        return null;
    }

    public void setName(String name) {
        if (!deleted) {
            this.name = name;
        } else {
            //throw error here
        }
    }

    public int getSolute() {
        if (!deleted) {
            return solute;
        } else {
            //throw error here
        }
        return -1;
    }

    public void setSolute(int solute) {
        if (!deleted) {
            this.solute = solute;
        } else {
            //throw error here
        }
    }
}
