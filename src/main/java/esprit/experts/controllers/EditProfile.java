package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditProfile implements Initializable {
    @FXML
    public TextField firstname;
    @FXML
    public TextField lastname;
    @FXML
    private ImageView profileImage;

    @FXML
    private Label nameLabel;


    @FXML
    private Button editButton;
    @FXML
    private ComboBox<String> role;

    @FXML
    private ComboBox<String> sex;

    @FXML
    private TextField status;
    @FXML
    private TextField email;
    private User user;
    private User userConnected ;


    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$");
    private String[] roles = {"SUPERVISOR", "ADMIN", "AUDITEUR"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userConnected = MainLayoutController.getUser();
        role.getItems().addAll(roles);
    }

    public void getUserProfile(Long id) {
        System.out.println(id);
        UserService us = new UserService();
        this.user = us.getById(id);
        if (Objects.isNull(this.user)) {
            System.out.println("User is not found");
        }
        this.populatePage();

    }

    private void populatePage() {
        System.out.println("Starting");
        nameLabel.setText(user.getFirstname() + " " + user.getLastname());
        role.setValue(user.getRole());
        sex.setValue(user.getSex());
        status.setText(user.getStatus());
        email.setText(user.getEmail());
        firstname.setText(user.getFirstname());
        lastname.setText(user.getLastname());

        String imagePath = user.getImagePath();
        try {
            InputStream inputStream = new FileInputStream(new File(imagePath));
            Image image = new Image(inputStream);
            profileImage.setImage(image);
        } catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
            // Handle file not found error
        }

    }

    @FXML
    public void editProfile() {
        // Save changes logic here
        if (validateFields()) {
            // Update user object with form values
            user.setFirstname(firstname.getText());
            user.setLastname(lastname.getText());
            user.setEmail(email.getText());
            user.setRole(role.getValue());
            user.setSex(sex.getValue());
            user.setStatus(status.getText());

            // Call UserService to update user
            UserService userService = new UserService();
            userService.Update(user);

            // Optionally, show success message or navigate to another view
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Profile Updated");
            alert.setHeaderText(null);
            alert.setContentText("Your profile has been updated successfully!");
            alert.showAndWait();
        } else {
            // Show error pop-up for validation errors
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill out all fields correctly.");
            alert.showAndWait();
        }
    }
    private boolean validateFields() {
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String userEmail = email.getText();
        String userStatus = status.getText();

        // Perform basic validation
        return !firstName.isEmpty() && !lastName.isEmpty() && EMAIL_REGEX.matcher(userEmail).matches() && !userStatus.isEmpty();
    }

    @FXML
    public void cancelEdit() {
        // Cancel logic here
    }
}
