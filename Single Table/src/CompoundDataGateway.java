import java.sql.*;
import java.util.ArrayList;
import config.ProjectConfig;

public class CompoundDataGateway extends Gateway {
    long compoundID;
    long elementID;
    protected static Connection m_dbConn = null;

    public CompoundDataGateway(long compound) {
        this.compoundID = compound;
    }

    public CompoundDataGateway(long compound, long element) {
        this.compoundID = compound;
        this.elementID = element;
    }

    public long getCompoundID() {
        return compoundID;
    }

    public void setCompoundID(long compoundID) {
        this.compoundID = compoundID;
    }

    public long getElementID() {
        return elementID;
    }

    public void setElementID(long elementID) {
        this.elementID = elementID;
    }

    public ArrayList<CompoundDataGateway> getAllCompounds() throws SQLException {
        ArrayList<CompoundDataGateway> allCompoundsList = new ArrayList<>();
        Statement statement = m_dbConn.createStatement();
        String stmt = new String("SELECT * FROM CompoundToElement");
        statement.execute(stmt);

        return allCompoundsList;
    }

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

    public boolean persist() {
        verifyExistence();
        try {
        } catch(Error e) {

        }

    }

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
