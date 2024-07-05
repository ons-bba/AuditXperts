package esprit.experts.controllers;

import esprit.experts.services.UserService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    private AnchorPane anchor;

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
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordErrorLabel.setText("Password is required.");
            return;
        }

        // Authenticate user
        boolean isAuthenticated = userService.authenticateUser(email, password);
        if (isAuthenticated) {
            fadeTransitionToMainLayout(email);
        } else {
            loginErrorLabel.setText("Invalid email or password.");
        }
    }

    private void fadeTransitionToMainLayout(String userEmail) {
        try {
            // Load the MainLayout.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/MainLayout.fxml"));
            Parent root = loader.load();

            // Access the controller and call setLoggedInUser
            MainLayoutController mainController = loader.getController();
            mainController.setLoggedInUser(userEmail);

            // Fade transition
            FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();

            // Create a new stage for the main layout
            Stage stage = (Stage) anchor.getScene().getWindow(); // Replace `button` with your actual button object
            stage.setScene(new Scene(root));
            stage.setTitle("Main Layout");
            stage.setFullScreen(true); // Set the stage to maximized mode
            // Show the main stage
            stage.show();

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
