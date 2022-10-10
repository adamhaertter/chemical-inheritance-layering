package datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MetalDataGateway extends ElementDataGateway {

    private long dissolvedBy = 0;

    /**
     * Constructs a row data gateway based on an existing id in the database
     * @param id primary key id
     */
    public MetalDataGateway(long id) {
        super(id);

        // Read from DB
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Metal WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            this.dissolvedBy = rs.getInt("dissolvedBy");

            if (!validate()) {
                this.id = -1;
                this.name = null;
                this.dissolvedBy = -1;
                System.out.println("No metal was found with the given id " + id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a row data gateway and a new instance of Metal in the DB and fills the given information into the appropriate tables.
     * @param name the name field of parent table Chemical
     * @param atomicNumber the atomicNumber field of parent table Element
     * @param atomicMass the atomicMass field of parent table Element
     * @param dissolvedByAcid the dissolvedBy foreign key of the Metal Table
     */
    public MetalDataGateway(String name, int atomicNumber, int atomicMass, long dissolvedByAcid) {
        super(name, atomicNumber, atomicMass);
        dissolvedBy = dissolvedByAcid;
    }

    // I'm not sure if this constructor will be useful in the long term
    public MetalDataGateway(long elementId, long dissolvedByAcid) {
        super(elementId);
        dissolvedBy = dissolvedByAcid;
    }

    protected boolean validate() {
        return super.validate() && this.dissolvedBy > 0;
    }

    /**
     * Queries the database for all metals dissolved by a given acid id and returns said ids as an ArrayList of longs.
     *
     * @param acidId the id of the acid that dissolves these metals
     * @return an ArrayList of metal ids
     */
    public static ArrayList<Long> getAllDissolvedBy(long acidId) {
        ArrayList<Long> metals = new ArrayList<Long>();
        try {
            Connection conn = setUpConnection();

            CallableStatement st = conn.prepareCall("SELECT * FROM Metal WHERE dissolvedBy = ?");
            st.setLong(1, acidId);
            ResultSet rs = st.executeQuery();
            //Go through all options in the ResultSet and save them
            while(rs.next()){
                metals.add(rs.getLong("dissolvedBy"));
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metals;
    }

    /** getters and setters **/
    public long getDissolvedBy() {
        return dissolvedBy;
    }

    public void setDissolvedBy(long dissolvedBy) {
        verifyExistence();
        this.dissolvedBy = dissolvedBy;
    }
}
