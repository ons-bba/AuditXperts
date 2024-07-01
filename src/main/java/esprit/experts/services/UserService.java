package esprit.experts.services;

import esprit.experts.entities.User;
import esprit.experts.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class UserService implements  IService<User>{


    @Override
    public void Create(User user) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "INSERT INTO users (firstname, lastname, email, password, role, status, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setString(5, user.getRole());
                statement.setString(6, user.getStatus());
                statement.setString(7, user.getImagePath());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error inserting user: " + e.getMessage());
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
        }
    }

    @Override
    public void Update(User o) {

    }

    @Override
    public List<User> Read() {
        return null;
    }

    @Override
    public void Delete(User o) {

    }

    @Override
    public User getById(long id) {
        return null;
    }
}
