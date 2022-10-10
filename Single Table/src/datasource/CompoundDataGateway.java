package datasource;

import dto.CompoundToElementDTO;

import java.sql.*;
import java.util.ArrayList;

/**
 * Used as a table data gateway for the CompoundToElement Table
 */
public class CompoundDataGateway extends ChemicalDataGateway {
    long compoundID;
    long elementID;
    protected static Connection m_dbConn = null;

    /**
     * Constructor that creates an entry based off of the compound ID
     *
     * @param compoundID - the compoundID
     */
    public CompoundDataGateway(long compoundID) throws SQLException {
        super(compoundID);
        this.compoundID = compoundID;
        try {
            CallableStatement statement = ChemicalDataGateway.m_dbConn.prepareCall("SELECT * FROM 'CompoundToElement'" +
                                                                "WHERE CompoundId = '" + compoundID + "'");
            ResultSet rs = statement.executeQuery();
            this.elementID = rs.getLong("ElementId");
        } catch(Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor that creates an entry based off of the compound ID and element ID
     *
     * @param compoundID - the compoundID
     * @param elementID - the elementID
     */
    public CompoundDataGateway(long compoundID, long elementID) throws SQLException {
        super(compoundID);
        this.compoundID = compoundID;
        this.elementID = elementID;
        persist(compoundID, elementID);
    }

    /**
     * Getter for compoundID
     * @return compoundID - the identification of the compound
     */
    public long getCompoundID() {
        if (!deleted) {
            return compoundID;
        } else {
            System.out.println("This Compound has been deleted.");
        }
        return -1;
    }

    /**
     * Setter for compoundID
     * @param compoundID - the identification of the compound
     */
    public void setCompoundID(long compoundID) {
        if (!deleted) {
            if (persist(compoundID, this.elementID))
                this.compoundID = compoundID;
        } else {
            System.out.println("This Compound has been deleted.");
        }
    }

    /**
     * Getter for elementID
     * @return elementID - the identification of the element
     */
    public long getElementID() {
        if (!deleted) {
            return elementID;
        } else {
            System.out.println("This Compound has been deleted.");
        }
        return -1;
    }

    /**
     * Setter for elementID
     * @param elementID - the identification of the element
     */
    public void setElementID(long elementID) {
        if (!deleted) {
            if (persist(this.compoundID, elementID))
                this.elementID = elementID;
        } else {
            System.out.println("This Chemical has been deleted.");
        }
    }

    /**
     * Getter for all the compounds in the table
     * @return allCompoundsList - a list of all the compounds in the table
     * @throws SQLException
     */
    public ArrayList<CompoundToElementDTO> getAllCompounds() throws SQLException {
        verifyExistence();
        ArrayList<CompoundToElementDTO> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        statement.execute("SELECT * FROM CompoundToElement");

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            CompoundToElementDTO compound = new CompoundToElementDTO(rs.getLong("CompoundID"),
                                                                    rs.getLong("ElementID"));
            compoundList.add(compound);
        }

        return compoundList;
    }

    /**
     * Gets all the Compounds that contain that Element
     * @param elemID - the identification of the element
     * @return compoundList - a list of compounds
     * @throws SQLException
     */
    public ArrayList<CompoundToElementDTO> getCompoundsByElementID(long elemID) throws SQLException {
        verifyExistence();
        ArrayList<CompoundToElementDTO> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        statement.execute("SELECT * FROM CompoundToElement WHERE ElementId=" + elemID);

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            CompoundToElementDTO compound = new CompoundToElementDTO(rs.getLong("CompoundID"), elemID);
            compoundList.add(compound);
        }

        return compoundList;
    }

    /**
     * Retrieves all the Elements in a Compound
     * @param compID - the identification of the compound
     * @return compoundList - a list of compounds
     * @throws SQLException
     */
    public ArrayList<CompoundToElementDTO> getCompoundElements(int compID) throws SQLException {
        verifyExistence();
        ArrayList<CompoundToElementDTO> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        statement.execute("SELECT * FROM CompoundToElement WHERE CompoundId=" + compID);

        ResultSet getCompounds = statement.getResultSet();
        while(getCompounds.next()) {
            CompoundToElementDTO compound = new CompoundToElementDTO(compID,
                                                                    getCompounds.getLong("ElementID"));
            compoundList.add(compound);
        }

        return compoundList;
    }

    /**
     * Takes all the elements currently in the object and pushes them to the DB
     *
     * @return true or false
     */
    public boolean persist(long compoundID, long elementID) {
        try {
            Statement statement = ChemicalDataGateway.m_dbConn.createStatement();
            statement.executeUpdate("UPDATE CompoundToElement SET CompoundId = '" + compoundID + "'," +
                    "ElementId = '" + elementID + "' WHERE CompoundId = '" + compoundID + "'");
        } catch (Exception ex) {
            // Fails because already exists
            return false;
        }
        return true;
    }

    /**
     * Clarifies if the row has been deleted or not
     */
    private boolean verifyExistence() {
        return (compoundID > 0 && elementID > 0);
    }
}
