package datasource;

import java.sql.*;
import java.util.ArrayList;

/**
 * Contains both the Row Data Gateway and Table Data Gateway functionality for the relation table between Compound and
 * Element. Row functions are done by an instance of this class, while the table functions are static methods.
 */
public class CompoundToElementDataGateway extends Gateway {

    private long compoundId = 0;
    private long elementId = 0;

    /**
     * Attempts to read a row with the given values from the DB. If it can't, we create that row in the DB.
     * Because of the structure of this table, both create and read functionality are combined into one constructor.
     *
     * @param conn connection to the DB
     * @param compoundId the compound id in the relation
     * @param elementId the element id in the relation
     */
    public CompoundToElementDataGateway(Connection conn, long compoundId, long elementId) {
        super(conn);
        deleted = false;
        id = compoundId; // For consistency, the id field of Gateway should be filled by either key.

        // Try to read from DB
        try {
            CallableStatement read = conn.prepareCall("SELECT * from CompoundsInElement WHERE elementId = ? AND compoundId = ?");
            read.setLong(1, elementId);
            read.setLong(2, compoundId);
            ResultSet rs = read.executeQuery();
            rs.next();
            this.elementId = rs.getLong("elementId");
            this.compoundId = rs.getLong("compoundId");

            if (!validate()) {
                // Unable to find that row, so we have to create it
                Statement create = conn.createStatement();
                String addEntry = "INSERT INTO CompoundsInElement" +
                        "(elementId, compoundId) VALUES ('" +
                        elementId + "','" + compoundId + "')";
                create.executeUpdate(addEntry);

                // Reassign appropriate values to instance variables
                this.elementId = elementId;
                this.compoundId = compoundId;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks the validity of the information in the row.
     *
     * @return Whether the current columns for this row have valid values
     */
    private boolean validate() {
        return this.compoundId >= 0 && this.elementId >= 0;
    }

    /**
     * Returns an ArrayList of compound ids for any compound in the database containing a given element
     *
     * @param elementId the element to search for
     * @return the List of compound ids containing the given element
     */
    public static ArrayList<Long> getCompoundsContaining(long elementId) {
        ArrayList<Long> compounds = new ArrayList<Long>();
        try {
            Connection conn = setUpConnection();

            CallableStatement st = conn.prepareCall("SELECT * FROM CompoundsInElement WHERE elementId = ?");
            st.setLong(1, elementId);
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            while(rs.next()){
                compounds.add(rs.getLong("compoundId"));
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return compounds;
    }

    /**
     * Produces an ArrayList of element ids contained in a single given compound
     *
     * @param compoundId the id of the compound to check
     * @return ArrayList of element ids
     */
    public static ArrayList<Long> getElementsInCompound(long compoundId) {
        ArrayList<Long> elements = new ArrayList<Long>();
        try {
            Connection conn = setUpConnection();

            CallableStatement st = conn.prepareCall("SELECT * FROM CompoundsInElement WHERE compoundId = ?");
            st.setLong(1, compoundId);
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            while(rs.next()){
                elements.add(rs.getLong("compoundId"));
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return elements;
    }

}