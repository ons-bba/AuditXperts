package esprit.experts.services;

import esprit.experts.entities.User;
import esprit.experts.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public List<User> read() {
        List<User> users = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setLastname(resultSet.getString("lastname"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setStatus(resultSet.getString("status"));
                    user.setImagePath(resultSet.getString("image"));
                    users.add(user);
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving users: " + e.getMessage());
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
        }
        return users;
    }

    @Override
    public void Delete(User o) {

    }

    @Override
    public User getById(long id) {
        return null;
    }

    public User getUserByEmail(String email) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT * FROM users WHERE email = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // User found, retrieve data and create User object
                    User user = new User();
                    user.setId(resultSet.getLong("id")); // Assuming 'id' is the column name
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setLastname(resultSet.getString("lastname"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setStatus(resultSet.getString("status"));
                    user.setImagePath(resultSet.getString("image"));
                    return user;
                } else {
                    // User with given email not found
                    return null;
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving user: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
            return null;
        }
    }


    public boolean authenticateUser(String email, String password) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // true if user exists with given email and password
            } catch (SQLException e) {
                System.out.println("Error authenticating user: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
            return false;
        }
    }
}
