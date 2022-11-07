package datasource;

import java.sql.*;
import java.util.ArrayList;

/**
 * Used as a table data gateway for the CompoundToElement Table
 */
public class CompoundDataGateway extends ChemicalDataGateway {
    long compoundID;
    long elementID;

    /**
     * Constructor that creates an entry based off of the compound ID and element ID
     *
     * @param compoundID - the compoundID
     * @param elementID - the elementID
     */
    public CompoundDataGateway(Connection conn, long compoundID, long elementID) {
        super(conn, compoundID);
        this.compoundID = compoundID;

        // Unable to find that row, so we have to create it
        try {
            Statement create = conn.createStatement();
            String addEntry = "INSERT INTO CompoundToElement" +
                    "(ElementID, CompoundID) VALUES ('" +
                    elementID + "','" + compoundID + "')";
            create.executeUpdate(addEntry);

            // Reassign appropriate values to instance variables
            this.elementID = elementID;
            this.compoundID = compoundID;
        } catch(SQLException sqlex2) {
            sqlex2.printStackTrace();
        }
    }

    /**
     * Gets all the Compounds that contain that Element
     * @param elemID - the identification of the element
     * @return compoundList - a list of compounds
     */
    public static ArrayList<Long> getCompoundsContaining(long elemID) {
        ArrayList<Long> compoundList = new ArrayList<>();
        try {
            Connection conn = setUpConnection();

            CallableStatement st = conn.prepareCall("SELECT * FROM CompoundToElement WHERE ElementId = ?");
            st.setLong(1, elemID);
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            //Multiple relations can exist for compounds that have multiple of the same element
            //We must make sure that there aren't duplicates
            while(rs.next()){
                long compID = rs.getLong("CompoundID");
                boolean alreadyInList = false;
                for(Long l: compoundList) {
                    if(l == compID) {
                        alreadyInList = true;
                    }
                }
                if(!alreadyInList) {
                    compoundList.add(compID);
                }
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return compoundList;
    }

    /**
     * Retrieves all the Elements in a Compound
     * @param compID - the identification of the compound
     * @return compoundList - a list of compounds
     */
    public static ArrayList<Long> getElementsInCompound(long compID) {
        ArrayList<Long> compoundList = new ArrayList<>();
        try {
            Connection conn = setUpConnection();
            CallableStatement st = conn.prepareCall("SELECT * FROM CompoundToElement WHERE compoundId = ?");
            st.setLong(1, compID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                compoundList.add(rs.getLong("elementID"));
            }
        } catch(SQLException sqlex) {
            sqlex.printStackTrace();
        }

        return compoundList;
    }

    /**
     * Checks the validity of the information in the row.
     *
     * @return Whether the current columns for this row have valid values
     */
    protected boolean validate() {
         return this.compoundID >= 0 && this.elementID >= 0;
    }
}
