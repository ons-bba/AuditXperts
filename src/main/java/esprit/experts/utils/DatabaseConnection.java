package esprit.experts.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    final private static String url = "jdbc:mysql://localhost:3306/ProjectPi";
    final private static String user = "root";
    final private static String password = "";

    public static void connectDB() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}