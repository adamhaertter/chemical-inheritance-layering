import config.ProjectConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class MetalDataGatewaysTest {
    private Connection conn;

    /**
     * This creates our connection and makes sure that we don't actually commit any changes to the DB
     * TODO: Create test DB
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        this.conn.setAutoCommit(false);
        // Insert Test Data
        Statement stmnt = this.conn.createStatement();
    }

    /**
     * Rolls back any changes that we made. Also closes our current connection
     * @throws SQLException
     */
    @AfterEach
    public void tearDown() throws SQLException {
        this.conn.rollback();
        this.conn.close();
    }

    @Test
    public void getName() {

    }

    @Test
    public void setName() {

    }

    @Test
    public void getAtomicNumber() {

    }

    @Test
    public void setAtomicNumber() {

    }

    @Test
    public void getAtomicMass() {
        
    }
}
