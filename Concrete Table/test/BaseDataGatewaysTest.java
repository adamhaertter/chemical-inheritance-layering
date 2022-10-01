import config.ProjectConfig;
import org.junit.Assert;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class BaseDataGatewaysTest {

    private Connection conn;


    @org.junit.jupiter.api.Test
    void setUp() throws SQLException {
        this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);

        conn.close();
    }

    @org.junit.jupiter.api.Test
    void tearDown() throws SQLException {
        conn.close();
    }

    @org.junit.jupiter.api.Test
    void getName() throws SQLException {

    }

    @org.junit.jupiter.api.Test
    void setName() {
    }

    @org.junit.jupiter.api.Test
    void getSolute() {
    }

    @org.junit.jupiter.api.Test
    void setSolute() {
    }
}