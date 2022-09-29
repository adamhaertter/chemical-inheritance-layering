import config.ProjectConfig;

import java.sql.*;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

class BaseDataGatewaysTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        try (Connection conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getName() {
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