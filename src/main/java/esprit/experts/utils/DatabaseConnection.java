package esprit.experts.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    final private static String url = "jdbc:mysql://localhost:3306/document";
    final private static String user = "document";
    final private static String password = "0000";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
