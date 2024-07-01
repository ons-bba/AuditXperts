package esprit.experts.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.regex.Pattern;

public class Register {

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleField;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private Label firstnameError;

    @FXML
    private Label lastnameError;

    @FXML
    private Label emailError;

    @FXML
    private Label passwordError;

    @FXML
    private Label roleError;

    @FXML
    private Label statusError;

    @FXML
    private ImageView imageView;

    @FXML
    private VBox imagePreviewContainer;

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$");

    @FXML
    private void handleRegisterAction(ActionEvent event) {
        boolean isValid = true;

        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleField.getValue();
        String status = statusField.getValue();

        // Clear previous error messages
        firstnameError.setText("");
        lastnameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        roleError.setText("");
        statusError.setText("");

        // Validate input
        if (firstname.isEmpty()) {
            firstnameError.setText("First name is required.");
            isValid = false;
        }

        if (lastname.isEmpty()) {
            lastnameError.setText("Last name is required.");
            isValid = false;
        }

        if (email.isEmpty() || !EMAIL_REGEX.matcher(email).matches()) {
            emailError.setText("Invalid email address.");
            isValid = false;
        }

        if (password.isEmpty() || !PASSWORD_REGEX.matcher(password).matches()) {
            passwordError.setText("Password must be at least 8 characters, include an uppercase letter, a lowercase letter, and a number.");
            isValid = false;
        }

        if (role == null || role.isEmpty()) {
            roleError.setText("Role is required.");
            isValid = false;
        }

        if (status == null || status.isEmpty()) {
            statusError.setText("Status is required.");
            isValid = false;
        }

        if (isValid) {
            // Handle user registration logic here (e.g., save to database)
            System.out.println("User registered: " + firstname + " " + lastname + ", Email: " + email + ", Role: " + role + ", Status: " + status);

            // Optionally, clear the form fields after successful registration
            firstnameField.clear();
            lastnameField.clear();
            emailField.clear();
            passwordField.clear();
            roleField.getSelectionModel().clearSelection();
            statusField.getSelectionModel().clearSelection();

            // Reset image preview
            imageView.setImage(null);
            imagePreviewContainer.setVisible(false);
        }
    }

    @FXML
    private void handleImageUploadAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imagePreviewContainer.setVisible(true);
        }
    }
}
