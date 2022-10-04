import java.sql.*;
import java.util.ArrayList;
import config.ProjectConfig;

public class CompoundDataGateway extends Gateway {
    long compoundID;
    long elementID;
    protected static Connection m_dbConn = null;

    /**
     *
     * @param compound - the compoundID
     */
    public CompoundDataGateway(long compound) {
        super();
        this.compoundID = compound;
        try {
            CallableStatement statement = conn.prepareCall("SELECT * FROM 'CompoundToElement'" +
                                                                "WHERE CompoundId = '" + CompoundID + "'");
            ResultSet rs = statement.executeQuery();
            this.elementID = rs.getLong("ElementId");
            persist();
        } catch(Exception ex) {
            // Some other error (There is not an error if the entry doesn't exist)
        }
    }

    /**
     *
     * @param compound - the compoundID
     * @param element - the elementID
     */
    public CompoundDataGatdeway(long compound, long element) {
        super();
        this.compoundID = compound;
        this.elementID = element;
        persist();
    }

    /**
     * Getter for compoundID
     * @return compoundID - the identification of the compound
     */
    public long getCompoundID() {
        return compoundID;
    }

    /**
     * Setter for compoundID
     * @param compoundID - the identification of the compound
     */
    public void setCompoundID(long compoundID) {
        this.compoundID = compoundID;
    }

    /**
     * Getter for elementID
     * @return elementID - the identification of the element
     */
    public long getElementID() {
        return elementID;
    }

    /**
     * Setter for elementID
     * @param elementID - the identification of the element
     */
    public void setElementID(long elementID) {
        this.elementID = elementID;
    }

    /**
     * Getter for all the compounds in the table
     * @return allCompoundsList - a list of all the compounds in the table
     * @throws SQLException
     */
    public ArrayList<CompoundDataGateway> getAllCompounds() throws SQLException {
        ArrayList<CompoundDataGateway> allCompoundsList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        String stmt = new String("SELECT * FROM CompoundToElement");
        statement.execute(stmt);

        return allCompoundsList;
    }

    /**
     * Getter f
     * @param elemID - the identification of the element
     * @return compoundList - a list of compounds
     * @throws SQLException
     */
    public ArrayList<CompoundDataGateway> getCompoundsByElementID(long elemID) throws SQLException {
        ArrayList<CompoundDataGateway> compoundList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        String stmt = new String("SELECT * FROM CompoundToElement WHERE ElementId=" + elemID);
        statement.execute(stmt);

        ResultSet getCompounds = statement.getResultSet();
        while(getCompounds.next()) {
            CompoundDataGateway compound = new CompoundDataGateway(getCompounds.getLong("CompoundID"),
                                                                                                        elemID);
            compoundList.add(compound);
        }

        return compoundList;
    }

    /**
     *
     * @param compID - the identification of the compound
     * @return compoundList - a list of compounds
     * @throws SQLException
     */
    public ArrayList<CompoundDataGateway> getCompoundElements(int compID) throws SQLException {
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
     *
     * @return true or false
     */
    public boolean persist() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE CompoundToElement SET CompoundId = '" + compoundID + "'," +
                    "ElementId = '" + elementID + "' WHERE CompoundId = '" + compoundID + "'");
        } catch (Exception ex) {
            // Fails because already exists
            return false;
        }
        return true;
    }

    /**
     *
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
