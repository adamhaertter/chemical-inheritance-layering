package utils;

import config.ProjectConfig;

import java.sql.*;

public class DatabaseMethods {
    public void addBase(String name, long solute) throws SQLException {
        try (Connection conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
             CallableStatement statement = conn.prepareCall("CALL Base_Add(?, ?)")) {

            statement.setString(1, name);
            statement.setLong(2, solute);

            statement.execute();
        }
    }
}