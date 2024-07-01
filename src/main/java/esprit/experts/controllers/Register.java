package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import esprit.experts.utils.DatabaseConnection;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private ImageView imageView;

    @FXML
    private VBox imagePreviewContainer;

    private File selectedImageFile;

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
        String status = "ACTIVE"; // Default status

        // Clear previous error messages
        firstnameError.setText("");
        lastnameError.setText("");
        emailError.setText("");
        passwordError.setText("");
        roleError.setText("");

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

        if (isValid) {
            String imagePath = null;
            if (selectedImageFile != null) {
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String extension = getFileExtension(selectedImageFile);
                String newImageName = "image_" + timestamp + "." + extension;

                File dest = new File("src/main/resources/images/" + newImageName);
                try {
                    // Ensure the directory exists
                    Files.createDirectories(dest.getParentFile().toPath());
                    Files.copy(selectedImageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    imagePath = "src/main/resources/images/" + newImageName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            User newUser = new User(firstname, lastname, email, password, role, status, imagePath);
            UserService userService = new UserService();
            userService.Create(newUser);

            // Clear the form fields after successful registration
            firstnameField.clear();
            lastnameField.clear();
            emailField.clear();
            passwordField.clear();
            roleField.getSelectionModel().clearSelection();
            imageView.setImage(null);
            imagePreviewContainer.setVisible(false);
            selectedImageFile = null;
        }
    }

    @FXML
    private void handleImageUploadAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imagePreviewContainer.setVisible(true);
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex == -1) {
            return "";
        }
        return name.substring(lastIndex + 1);
    }
}
