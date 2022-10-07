package datasource;

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
     * @param compound - the compoundID
     */
    public CompoundDataGateway(long compound) throws SQLException {
        super(compound);
        this.compoundID = compound;
        try {
            CallableStatement statement = ChemicalDataGateway.m_dbConn.prepareCall("SELECT * FROM 'CompoundToElement'" +
                                                                "WHERE CompoundId = '" + compound + "'");
            ResultSet rs = statement.executeQuery();
            this.elementID = rs.getLong("ElementId");
            persist();
        } catch(Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     * Constructor that creates an entry based off of the compound ID and element ID
     *
     * @param compound - the compoundID
     * @param element - the elementID
     */
    public CompoundDataGateway(long compound, long element) throws SQLException {
        super(compound);
        this.compoundID = compound;
        this.elementID = element;
        persist();
    }

    /**
     * Getter for compoundID
     * @return compoundID - the identification of the compound
     */
    public long getCompoundID() {
        verifyExistence();
        return compoundID;
    }

    /**
     * Setter for compoundID
     * @param compoundID - the identification of the compound
     */
    public void setCompoundID(long compoundID) {
        verifyExistence();
        this.compoundID = compoundID;
    }

    /**
     * Getter for elementID
     * @return elementID - the identification of the element
     */
    public long getElementID() {
        verifyExistence();
        return elementID;
    }

    /**
     * Setter for elementID
     * @param elementID - the identification of the element
     */
    public void setElementID(long elementID) {
        verifyExistence();
        this.elementID = elementID;
    }

    /**
     * Getter for all the compounds in the table
     * @return allCompoundsList - a list of all the compounds in the table
     * @throws SQLException
     */
    public ArrayList<CompoundDataGateway> getAllCompounds() throws SQLException {
        verifyExistence();
        ArrayList<CompoundDataGateway> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        String stmt = new String("SELECT * FROM CompoundToElement");
        statement.execute(stmt);

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            CompoundDataGateway compound = new CompoundDataGateway(rs.getLong("CompoundID"));
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
    public ArrayList<CompoundDataGateway> getCompoundsByElementID(long elemID) throws SQLException {
        verifyExistence();
        ArrayList<CompoundDataGateway> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        String stmt = new String("SELECT * FROM CompoundToElement WHERE ElementId=" + elemID);
        statement.execute(stmt);

        ResultSet rs = statement.getResultSet();
        while(rs.next()) {
            CompoundDataGateway compound = new CompoundDataGateway(rs.getLong("CompoundID"), elemID);
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
    public ArrayList<CompoundDataGateway> getCompoundElements(int compID) throws SQLException {
        verifyExistence();
        ArrayList<CompoundDataGateway> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        String stmt = new String("SELECT * FROM CompoundToElement WHERE CompoundId=" + compID);
        statement.execute(stmt);

        ResultSet getCompounds = statement.getResultSet();
        while(getCompounds.next()) {
            CompoundDataGateway compound = new CompoundDataGateway(compID,
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
    public boolean persist() {
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
    private void verifyExistence() {
        if (deleted)
            try {
                throw new Exception("This item has been deleted.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
