package esprit.experts.services;

import esprit.experts.entities.User;
import esprit.experts.utils.DatabaseConnection;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserService implements  IService<User>{


    @Override
    public void Create(User user) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "INSERT INTO users (firstname, lastname, email, password, role, status, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());
                statement.setString(3, user.getEmail());
                // Hash the password before storing it
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                statement.setString(4, hashedPassword);
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
    public void Update(User user) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "UPDATE users SET firstname=?, lastname=?, email=?, password=?, role=?, status=?, image=?, sex=? WHERE id=?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setString(5, user.getRole());
                statement.setString(6, user.getStatus());
                statement.setString(7, user.getImagePath());
                statement.setString(8, user.getSex());
                statement.setLong(9, user.getId());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("User with ID " + user.getId() + " was updated successfully!");
                }
            } catch (SQLException e) {
                System.out.println("Error updating user: " + e.getMessage());
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
        }
    }

    @Override
    public List<User> read() throws SQLException {
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
    public User getById(long id) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setLastname(resultSet.getString("lastname"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getString("role"));
                    user.setStatus(resultSet.getString("status"));
                    user.setImagePath(resultSet.getString("image"));
                    user.setSex(resultSet.getString("sex"));
                    return user;
                } else {
                    // User with given ID not found
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

    public User getUserByEmail(String email) throws SQLException {
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
                    user.setSex(resultSet.getString("Sex"));
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


    public boolean authenticateUser(String email, String password) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT password FROM users WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    // Check if the provided plain text password matches the hashed password
                    return BCrypt.checkpw(password, hashedPassword);
                } else {
                    return false; // User with given email not found
                }
            } catch (SQLException e) {
                System.out.println("Error authenticating user: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
            return false;
        }
    }

    public void updatePassword(long userId, String oldPassword, String newPassword) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            // First, check if the old password matches the current password in the database
            if (authenticateUserById(userId, oldPassword)) {
                String sql = "UPDATE users SET password = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newPassword);
                    statement.setLong(2, userId);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Password updated successfully for user with ID " + userId);
                        showInformationAlert("Password Updated", "Password updated successfully!");
                    } else {
                        showErrorAlert("Update Failed", "Failed to update password. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error updating password: " + e.getMessage());
                    showErrorAlert("Update Error", "Failed to update password. Error: " + e.getMessage());
                }
            } else {
                showErrorAlert("Authentication Failed", "Old password does not match. Password update failed.");
            }
        } else {
            showErrorAlert("Database Error", "Database connection is null. Check your database connection.");
        }
    }

    private boolean authenticateUserById(long userId, String password) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT * FROM users WHERE id = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, userId);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // true if user exists with given ID and password
            } catch (SQLException e) {
                System.out.println("Error authenticating user: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
            return false;
        }
    }

    // Other methods for user management (getUserByEmail, authenticateUser, etc.)

    // Helper method to show an information alert
    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show an error alert
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
