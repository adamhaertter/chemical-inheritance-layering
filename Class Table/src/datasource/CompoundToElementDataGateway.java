package datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompoundToElementDataGateway extends Gateway {

    private long compoundId = 0;
    private long elementId = 0;

    /** only for finding what's already in the database **/
    public CompoundToElementDataGateway(long compoundId, long elementId) {
        super();
        this.elementId = elementId;
        this.compoundId = compoundId;
        deleted = false;
        id = compoundId; // For consistency, the id field of Gateway should be filled by either key.
    }

    private boolean validate() {
        return this.compoundId != 0 && this.elementId != 0;
    }

    /**
     * Returns an ArrayList of compound ids for any compound in the database containing a given element
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