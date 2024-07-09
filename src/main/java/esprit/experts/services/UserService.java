package esprit.experts.services;

import esprit.experts.entities.User;
import esprit.experts.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserService implements  IService<User>{
    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet resultSet;
    private Statement statement;

    @Override
    public void Create(User user) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "INSERT INTO users (firstname, lastname, email, password, role, status, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());
                statement.setString(3, user.getEmail());

                // Hash the password using bcrypt
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
                showErrorAlert("Error SQL : " , "Error inserting user: " + e.getMessage());
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
        }
    }
    public boolean createUser(User user) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "INSERT INTO users (firstname, lastname, email, password, role, status, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getFirstname());
                statement.setString(2, user.getLastname());
                statement.setString(3, user.getEmail());

                // Hash the password using bcrypt
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                statement.setString(4, hashedPassword);

                statement.setString(5, user.getRole());
                statement.setString(6, user.getStatus());
                statement.setString(7, user.getImagePath());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user was inserted successfully!");
                    return true; // Return true if insertion was successful
                } else {
                    return false; // Return false if no rows were inserted (insertion failed)
                }
            } catch (SQLException e) {
                System.out.println("Error inserting user: " + e.getMessage());
                showErrorAlert("Error SQL : " , "Error inserting user: " + e.getMessage());
                return false; // Return false in case of any SQL error
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
            return false; // Return false if database connection is null
        }
    }
    @Override
    public void Update(User user) {
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
                    user.setSex(resultSet.getString("sex"));

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


    public boolean authenticateUser(String email, String password) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "SELECT password FROM users WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    // Verify hashed password using BCrypt
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        return true; // Passwords match, user authenticated
                    } else {
                        return false; // Passwords do not match
                    }
                } else {
                    return false; // No user found with the given email
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
    public ObservableList<User> userListData() {

        ObservableList<User> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users";

         connection = DatabaseConnection.getConnection();


        try {
             prepare = connection.prepareStatement(sql);
             resultSet = prepare.executeQuery();
             User user;

            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                user.setStatus(resultSet.getString("status"));
                user.setImagePath(resultSet.getString("image"));
                user.setSex(resultSet.getString("sex"));
                listData.add(user);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    public boolean DeleteUser(User user) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, user.getId());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("User with ID " + user.getId() + " was deleted successfully!");
                    return true;
                } else {
                    System.out.println("User with ID " + user.getId() + " not found for deletion.");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Error deleting user: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Database connection is null. Check your database connection.");
            return false;
        }
    }
}
