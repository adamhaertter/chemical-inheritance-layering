package src.utils;

import net.dv8tion.jda.api.entities.*;

import java.sql.*;

public class DatabaseMethods {
    public void addBase(String name, long solute) throws SQLException {
        try (Connection conn = DriverManager.getConnection(BirthdayBotConfig.getDbUrl(), BirthdayBotConfig.getDbUser(), BirthdayBotConfig.getDbPassword());
             CallableStatement statement = conn.prepareCall("CALL Base_Add(?, ?)")) {

            statement.setString(1, name);
            statement.setLong(2, solute);

            statement.execute();
        }
    }
}