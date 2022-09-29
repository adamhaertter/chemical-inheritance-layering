package utils;

import config.ProjectConfig;

import java.sql.*;

public class DatabaseMethods {

    Connection conn;

    public DatabaseMethods() {
        try {
            this.conn = DriverManager.getConnection(ProjectConfig.DatabaseURL, ProjectConfig.DatabaseUser, ProjectConfig.DatabasePassword);
        } catch (Exception ex) {
            System.out.println("Error connecting to database");
        }
    }

    public void addBase(String name, long solute) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            String addBase = new String("INSERT INTO CARTEL" +
                    "(name, solute) VALUES ('" +
                    name + "','" + solute + "')");
            statement.executeUpdate(addBase);
        } catch (Exception ex) {
            System.out.println("Base already exists");
        }
    }

    public void getBase(long id) throws SQLException {
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from Base WHERE id = ?");
            statement.setLong(1, id);
            statement.execute();
        } catch (Exception ex) {
            System.out.println("Base is not in the DB");
        }
    }

    public void updateBase(long id, String name, long solute) throws SQLException {
        try {
            Statement statement = conn.createStatement();
            String updateBase = new String("UPDATE Base SET name = '" + name + "', solute = '" + solute +
                    "' WHERE id = '" + id + "'");
            statement.executeUpdate(updateBase);
        } catch (Exception ex) {
            System.out.println("Base is not in the DB");
        }
    }
}