package utils;

import enums.TableEnums;

import java.sql.*;

public class ValidationUtils {

    /**
     * Check if the given solute is a valid solute
     *
     * @param conn the connection to the database
     * @param id   the id of the solute
     * @return true if the solute is valid, false otherwise
     */
    public static boolean doesSoluteExist(Connection conn, long id) {
        try {
            for (TableEnums.Table table : TableEnums.Table.values()) {
                if (checkIfSoluteExist(conn, id, table)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    /**
     * Check if the given solute is a valid solute for the given table
     *
     * @param conn  the connection to the database
     * @param id    the id of the solute
     * @param table the table to check
     * @return true if the solute is valid, false otherwise
     */
    private static boolean checkIfSoluteExist(Connection conn, long id, TableEnums.Table table) throws SQLException {
        try {
            CallableStatement statement = conn.prepareCall("SELECT * from " + table + " WHERE id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            return false;
        }
    }
}
