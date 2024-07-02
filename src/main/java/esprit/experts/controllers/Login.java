package esprit.experts.controllers;

import esprit.experts.services.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Label loginErrorLabel;

    private UserService userService;

    public Login() {
        userService = new UserService();
    }

    @FXML
    private void initialize() {
        // Initialize error labels
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        loginErrorLabel.setText("");
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Reset error labels
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        loginErrorLabel.setText("");

        // Validate email
        if (email.isEmpty()) {
            emailErrorLabel.setText("Email is required.");
        }

        // Validate password
        if (password.isEmpty()) {
            passwordErrorLabel.setText("Password is required.");
        }

        // Check if any error labels have been set
        if (!emailErrorLabel.getText().isEmpty() || !passwordErrorLabel.getText().isEmpty()) {
            // If there are errors, return without attempting to authenticate
            return;
        }

        // Authenticate user
        boolean isAuthenticated = userService.authenticateUser(email, password);
        if (isAuthenticated) {
            showMainLayout(email);
        } else {
            loginErrorLabel.setText("Invalid email or password.");
        }
    }

    private void showMainLayout(String userEmail) {
        try {
            // Load the MainLayout.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/MainLayout.fxml"));
            BorderPane root = loader.load();

            // Access the controller and call setLoggedInUser
            MainLayoutController mainController = loader.getController();
            mainController.setLoggedInUser(userEmail);

            // Show the scene containing the root layout
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = new Stage(); // Create a new stage instance

            // Set scene
            stage.setScene(scene);

            // Show the stage
            stage.show();

            // Maximize the stage after it's shown
            Platform.runLater(() -> stage.setMaximized(true));

            // Close the login stage if needed
            Stage loginStage = (Stage) emailField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading main layout.");
        }
    }





    @FXML
    private void handleCancelAction(ActionEvent event) {
        emailField.clear();
        passwordField.clear();
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        loginErrorLabel.setText("");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
