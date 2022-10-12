package datasource;

import config.ProjectConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class CompoundToElementDataGatewaysTest {
    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * Also inserts our testing data
     * TODO: Create test DB
     *
     * @throws SQLException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
        String insertTestCompound = "INSERT INTO Compound VALUES (1, 'TestCompound')";
        String insertTestElement1 = "INSERT INTO Element VALUES (2, 'TestElement1', 10, 10.5)";
        String insertTestRelation1 = "INSERT INTO CompoundToElement VALUES (1, 2)";
        stmnt.executeUpdate(insertTestCompound);
        stmnt.executeUpdate(insertTestElement1);
        stmnt.executeUpdate(insertTestRelation1);
    }

    /**
     * Rolls back any changes that we made. Also closes our current connection
     *
     * @throws SQLException
     */
    @AfterEach
    public void tearDown() throws SQLException {
        this.conn.rollback();
        this.conn.close();
    }

    /**
     * Check to see if we can delete a relation from the DB
     *
     * @throws SQLException
     */
    @Test
    public void delete() throws Exception {
        CompoundToElementDataGateways relation = new CompoundToElementDataGateways(conn, 1, 2);
        relation.delete();
        Statement stmnt = this.conn.createStatement();
        String selectTestRelation = "SELECT * FROM CompoundToElement WHERE compoundId = 1";
        ResultSet rs = stmnt.executeQuery(selectTestRelation);
        Assertions.assertFalse(rs.next());
    }

}
